package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext; // Import SecurityContext
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository; // Import
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; // Adicionar HttpServletResponse
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TecnicoRepository tecnicoRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials,
                                   HttpServletRequest request,
                                   HttpServletResponse response) { // Adicionar HttpServletResponse
        String matricula = credentials.get("matricula");
        String senha = credentials.get("senha");

        if (matricula == null || senha == null) {
            return ResponseEntity.badRequest().body("Matrícula e senha são obrigatórios.");
        }

        try {
            Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(matricula, senha);
            Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);

            // Cria um novo contexto de segurança ou obtém o existente
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationResult);
            SecurityContextHolder.setContext(securityContext); // Define o novo contexto para a thread atual

            // Salva explicitamente o contexto na sessão HTTP
            // O HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY é "SPRING_SECURITY_CONTEXT"
            HttpSession session = request.getSession(true); // true para criar se não existir
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            // Log para verificar
            System.out.println(">>> AuthController: Login bem-sucedido. Sessão ID: " + session.getId());
            System.out.println(">>> AuthController: SecurityContext salvo na sessão: " + securityContext);


            UserDetails userDetails = (UserDetails) authenticationResult.getPrincipal();
            Optional<TecnicoModel> tecnicoOptional = tecnicoRepository.findByMatricula(userDetails.getUsername());

            if (tecnicoOptional.isPresent()) {
                TecnicoModel tecnicoAutenticado = tecnicoOptional.get();
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("idTecnico", tecnicoAutenticado.getIdTecnico());
                responseBody.put("nome", tecnicoAutenticado.getNome());
                responseBody.put("matricula", tecnicoAutenticado.getMatricula());
                responseBody.put("isAdm", tecnicoAutenticado.isAdm());

                return ResponseEntity.ok(responseBody);
            } else {
                return ResponseEntity.status(500).body("Erro ao recuperar detalhes do técnico após login.");
            }

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Matrícula ou senha inválidos.");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
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