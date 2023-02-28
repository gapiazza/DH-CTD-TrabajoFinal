package com.dh.dhbooking.service;

import com.dh.dhbooking.dto.UserDTO;
import com.dh.dhbooking.exception.ResourceNotFoundException;
import com.dh.dhbooking.model.UserEntity;
import com.dh.dhbooking.repository.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService  implements  IUserService{

    private final IUserRepository userRepository;

    private final IProductService productService;
    private final ObjectMapper mapper;


    private PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, IProductService productService, ObjectMapper mapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productService = productService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setCreatedAt(LocalDateTime.now());
       UserEntity user= userRepository.save(mapper.convertValue(userDTO, UserEntity.class));
       return mapper.convertValue(user,UserDTO.class);
    }

    @Override
    public void deleteUser(Integer id) throws ResourceNotFoundException {
        UserEntity userEntity =userRepository.findById(id).orElseThrow(()->{return new ResourceNotFoundException("No encontrado");});
        userEntity.setDeletedAt(LocalDateTime.now());
        userRepository.save(userEntity);
    }

    @Override
    public void updateUser(UserDTO userDTO) throws ResourceNotFoundException {
        UserEntity userEntity =userRepository.findById(userDTO.getId()).orElseThrow(()->{return new ResourceNotFoundException("Recurso no encontrado.");});
             userEntity.setName(userDTO.getName());
             userEntity.setLastname(userDTO.getLastname());
             userEntity.setEmail(userDTO.getEmail());
             userRepository.save(userEntity);
    }

    @Override
    public UserDTO getUserById(Integer id) throws ResourceNotFoundException {
        UserEntity userEntity =userRepository.findById(id).orElseThrow(()->{return new ResourceNotFoundException("Recurso no encontrado.");});
        if (userEntity.getDeletedAt()==null)
            return mapper.convertValue(userEntity,UserDTO.class);
        else
            throw new ResourceNotFoundException("Recurso borrado");
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOList=new ArrayList<>();

        for (UserEntity userEntity :userRepository.findAll())
            userDTOList.add(mapper.convertValue(userEntity,UserDTO.class));

        return userDTOList;
    }

    @Override
    public UserEntity findOneByEmail(String email)
    {
        return userRepository.findOneByEmail(email);
    }

    private String getUserLogin(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = null;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }
        String userName = userDetails.getUsername();
        return userName;
    }
    public  Integer getIdUser(){
        String userName=this.getUserLogin();
        UserEntity user=new UserEntity();
        user=this.findOneByEmail(userName);
        Integer userId=user.getId();
        return userId;
    }

    public Integer generatedNumberRandom(){
       return  (int)(Math.random()*(999999-100000+1)+100000);
    }

    public void resetPassword(String email,String password){
        UserEntity user=userRepository.findOneByEmail(email);
        user.setPassword(password);
        userRepository.save(user);
    }
}
