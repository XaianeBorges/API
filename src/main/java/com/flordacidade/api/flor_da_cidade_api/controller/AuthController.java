package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.LoginRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.TecnicoMapper;
import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.TecnicoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de Login e Logout")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TecnicoRepository tecnicoRepository;
    private final TecnicoMapper tecnicoMapper;

    @Operation(summary = "Autentica um técnico e cria uma sessão")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        String matricula = loginRequest.getMatricula();
        String senha = loginRequest.getSenha();

        if (matricula == null || senha == null) {
            return ResponseEntity.badRequest().body("Matrícula e senha são obrigatórios.");
        }

        try {
            Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(matricula, senha);

            Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationResult);
            SecurityContextHolder.setContext(securityContext);

            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            UserDetails userDetails = (UserDetails) authenticationResult.getPrincipal();
            TecnicoModel tecnicoAutenticado = tecnicoRepository.findByMatricula(userDetails.getUsername())
                    .orElseThrow(() -> new InternalAuthenticationServiceException("Técnico não encontrado."));

            return ResponseEntity.ok(tecnicoMapper.toLoginResponseDTO(tecnicoAutenticado));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário desabilitado.");
        } catch (InternalAuthenticationServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno de autenticação.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout realizado com sucesso.");
    }
}