package com.example.network.services;

import com.example.network.dtos.AuthenticationResponseDTO;
import com.example.network.dtos.LoginRequestDTO;
import com.example.network.dtos.UserDetailsDTO;
import com.example.network.dtos.user.UserCreateRequestDto;
import com.example.network.dtos.user.UserResponseDto;
import com.example.network.dtos.user.UserUpdateRequestDto;
import com.example.network.entities.EmailObject;
import com.example.network.entities.User;
import com.example.network.entities.enums.Role;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.ForbiddenCustomException;
import com.example.network.exceptions.NotFoundCustomException;
import com.example.network.mappers.UserMapper;
import com.example.network.repositories.UserRepository;
import com.example.network.services.authorization.UserResolver;
import com.example.network.services.authorization.UserRoleValidator;
import com.example.network.utils.Encryption;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRoleValidator roleValidator;

    @Autowired
    UserResolver userResolver;

    @Autowired
    EmailService emailService;

    @Value("${spring.mail.host}")
    private String host;


    @Override
    public UserResponseDto createUser(UserCreateRequestDto userCreateRequestDto) throws ForbiddenCustomException {
        User user= new User();
        user.setName(userCreateRequestDto.name());
        user.setUsername(userCreateRequestDto.username());
        user.setPassword(Encryption.getPassEncoded(userCreateRequestDto.password()));
        user.setEmail(userCreateRequestDto.email());
        user.setRole(Role.ROLE_USER);
        for (User userDB: userRepository.findAll()){
            if(user.getUsername().equals(userDB.getUsername()) || user.getEmail().equals(userDB.getEmail())){
                throw new ForbiddenCustomException("User is already exists!");
            }
        }
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto) throws AuthorizationCustomException {
        User userValidator = userRepository.findByUsername(userResolver.getUsername());

        roleValidator.validateUserWithUsername(userValidator.getUsername());
        User user=userMapper.toEntityUpdate(userUpdateRequestDto);
        user.setPassword(Encryption.getPassEncoded(userUpdateRequestDto.password()));
        user.setId(userValidator.getId());
        user.setRole(Role.ROLE_USER);

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto deleteUser(Long id) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new NotFoundCustomException("User not found");
        }
        roleValidator.validateAdmin();
        userRepository.delete(optionalUser.get());
        return userMapper.toResponseDto(optionalUser.get());
    }

    @Override
    public UserResponseDto getById(Long id) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new NotFoundCustomException("User not found");
        }
        roleValidator.validateUserAndAdmin();
        return userMapper.toResponseDto(optionalUser.get());
    }

    @Override
    public List<UserResponseDto> getAll() throws AuthorizationCustomException {
        List<User> users=userRepository.findAll();
        roleValidator.validateAdmin();
        return userMapper.toResponseDtoList(users);
    }

    @Override
    public List<User> search(String name) {

        List<User> userList = userRepository.findAll();
        List<User> users = new ArrayList<>();

        if (name == null && name.isEmpty()){
            return userList;
        }
        users.addAll(userList.stream().filter(user -> user.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList()));
        return users;
    }

    @Override
    public AuthenticationResponseDTO  login(LoginRequestDTO loginRequestDTO) throws NotFoundCustomException, AuthorizationCustomException {
        if(!userRepository.existsByUsername(loginRequestDTO.getUsername())){
            throw new NotFoundCustomException("User not found");
        }
        User user = userRepository.findByUsername(loginRequestDTO.getUsername());
        if(user.getUsername().equals(loginRequestDTO.getUsername()) && user.getPassword().equals(loginRequestDTO.getPassword())){
            throw new AuthorizationCustomException("Bad credentials");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );

        UserResponseDto userResponseDto = userMapper.toResponseDto(user);
        UserDetailsDTO userDetailsDTO = userMapper.toUserDatailsDTO(userResponseDto);
        var jwtToken = jwtService.generateToken(userDetailsDTO.getExtraClaims(), userDetailsDTO);
        return AuthenticationResponseDTO
                .builder()
                .token(jwtToken)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public UserResponseDto forgotPassword(String email) throws Exception {
        if(userRepository.existsByEmail(email)){
            throw new NotFoundCustomException("User with email " + email + " not found.");
        }
        User foundUser = userRepository.findByEmail(email);

        UserResponseDto userResponseDto = userMapper.toResponseDto(foundUser);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userResponseDto.email());
        claims.put("username", userResponseDto.username());
        String token = jwtService.generateActivationToken(claims);
        String resetPasswordLink = host + "/reset-password";
        EmailObject emailObject = new EmailObject();
        emailObject.setTo(userResponseDto.email());
        emailObject.setSubject("Reset Password");
        emailObject.setText("Dear"  + userResponseDto.username() +
                ", please click on <a href=\"" + resetPasswordLink + "?token=" + token + "\">link</a> to reset password");
        emailService.sendSimpleMessage(emailObject);

        return userResponseDto;
    }

    public UserResponseDto resetPassword(String password, String token) {
        Map<String, Object> claims = jwtService.extractAllClaims(token);
        if (claims.get("email") == null || claims.get("username") == null || claims.get("matricola") == null) {
            throw new AuthenticationServiceException("Invalid token");
        }
        String email = claims.get("email").toString();
        String username = claims.get("username").toString();
        User user = userRepository.findByEmailAndUsername(email, username);
        if (user.getEmail().isEmpty() && user.getUsername().isEmpty()) {
            throw new AuthenticationServiceException("Invalid token");
        }
        String encodedPassword = Encryption.getPassEncoded(password);
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }
}
