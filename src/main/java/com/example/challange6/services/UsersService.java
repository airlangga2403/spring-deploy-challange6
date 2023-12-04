package com.example.challange6.services;


import com.example.challange6.dto.user.request.UserRequestChangePWDTO;
import com.example.challange6.dto.user.request.UserRequestDTO;
import com.example.challange6.dto.user.response.UserResponseByIdDTO;
import com.example.challange6.dto.user.response.UserResponseChangePW;
import com.example.challange6.dto.user.response.UserResponseListDTO;
import com.example.challange6.models.ERole;
import com.example.challange6.models.Role;
import com.example.challange6.models.Users;
import com.example.challange6.repository.RoleRepository;
import com.example.challange6.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    public UserRequestDTO registerUser(UserRequestDTO userDTO) {
        // Check if User Exists
        Optional<Users> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isEmpty()) {
            Users newUser = new Users();
            newUser.setUsername(userDTO.getUsername());
            newUser.setEmailAddress(userDTO.getEmailAddress());

            // Encode Password using BCrypt
            String encodedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
            newUser.setPassword(encodedPassword);

            if (userDTO.getRoleUser() == 1) {
                Role buyerRole = roleRepository.findByName(ERole.ROLE_BUYER);
                if (buyerRole != null) {
                    newUser.getRoles().add(buyerRole);
                }
            } else if (userDTO.getRoleUser() == 2) {
                Role sellerRole = roleRepository.findByName(ERole.ROLE_SELLER);
                if (sellerRole != null) {
                    newUser.getRoles().add(sellerRole);
                }
            }

            Users registeredUser = userRepository.save(newUser);
            userDTO.setUuid(registeredUser.getId());
            return userDTO;
        } else {
            return null;
        }
    }

    public UserRequestDTO loginUser(UserRequestDTO userRequestDTO) {
        Optional<Users> user = userRepository.findByUsernameAndPassword(userRequestDTO.getUsername(),
                userRequestDTO.getPassword());
        return user.map(this::convertToUserDTO).orElse(null);
    }

    public UserResponseByIdDTO getUserByUUID(UUID uuid) {
        Optional<Users> userById = userRepository.findById(uuid);
        return userById.map(this::convertToUserResponseByUUID).orElse(null);
    }

    // Update newUsername based By Username and Password
    public UserResponseChangePW changePwByUsername(UserRequestChangePWDTO userDTO) {

        UserResponseChangePW userResponse = new UserResponseChangePW();
        if (userDTO.getNewPassword() != null && userDTO.getUsername() != null && userDTO.getPassword() != null) {
            Integer updateUser = userRepository.updatePasswordByUsername(userDTO.getUsername(), userDTO.getPassword(),
                    userDTO.getNewPassword());
            if (updateUser == 1) {
                userResponse.setMessage("SuccessFully Update Password");
                return userResponse;
            } else {
                userResponse.setMessage("Failed Update Password");
                return userResponse;
            }
        } else {
            userResponse.setMessage("Failed Update Password");
            return userResponse;
        }
    }

    // GET LIST AND FILTERS {
        //Menampilkan detail order dari user yang melakukan
        //order makanan.
    // }
    public List<UserResponseListDTO> getListUserOrderedFood() {
        List<Users> usersWithOrders = userRepository.findAll();

        return usersWithOrders.stream()
                .map(user -> new UserResponseListDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmailAddress(),
                        user.getOrders()
                ))
                .collect(Collectors.toList());
    }

    private UserRequestDTO convertToUserDTO(Users user) {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUuid(user.getId());
        userRequestDTO.setUsername(user.getUsername());
        userRequestDTO.setEmailAddress(user.getEmailAddress());
        return userRequestDTO;
    }

    private UserResponseByIdDTO convertToUserResponseByUUID(Users user) {
        UserResponseByIdDTO userRequestDTO = new UserResponseByIdDTO();
        userRequestDTO.setUserName(user.getUsername());
        userRequestDTO.setEmail(user.getEmailAddress());
        return userRequestDTO;
    }
}
