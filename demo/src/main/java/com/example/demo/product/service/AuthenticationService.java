package com.example.demo.product.service;
import com.example.demo.product.dto.AuthenticationRequestDTO;
import com.example.demo.product.dto.AuthenticationResponseDTO;
import com.example.demo.product.dto.RegisterRequestDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO register(RegisterRequestDTO request);
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);

}
