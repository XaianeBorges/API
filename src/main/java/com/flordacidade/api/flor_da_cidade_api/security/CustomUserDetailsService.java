// src/main/java/com/flordacidade/api/flor_da_cidade_api/security/CustomUserDetailsService.java
package com.flordacidade.api.flor_da_cidade_api.security;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String matricula) throws UsernameNotFoundException {
        TecnicoModel tecnico = tecnicoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new UsernameNotFoundException("Técnico não encontrado com matrícula: " + matricula));

        if (tecnico.getStatus() != TecnicoModel.TecnicoStatus.ATIVO) {
            throw new UsernameNotFoundException("Conta do técnico está inativa: " + matricula); // Ou LockedException, DisabledException
        }

        return new org.springframework.security.core.userdetails.User(
                tecnico.getMatricula(),
                tecnico.getSenha(), // A senha HASHADA do banco
                true, // enabled (baseado no status ATIVO)
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                getAuthorities(tecnico) // Método para obter as roles/authorities
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(TecnicoModel tecnico) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Role base para qualquer técnico autenticado
        authorities.add(new SimpleGrantedAuthority("ROLE_TECNICO"));

        if (tecnico.isAdm()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        // Adicione outras roles/authorities se necessário
        return authorities;
    }
}