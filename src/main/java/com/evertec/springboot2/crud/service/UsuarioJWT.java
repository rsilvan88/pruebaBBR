package com.evertec.springboot2.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.evertec.springboot2.crud.model.Usuario; // Asegúrate de importar la clase de la entidad Usuario
import com.evertec.springboot2.crud.repository.UsuarioRepository; // Importa el repositorio de Usuario

import java.util.Arrays;
import java.util.List;

@Service
public class UsuarioJWT implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository; // Inyecta el repositorio de Usuario

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username); // Busca el usuario por su nombre de usuario en la base de datos

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        String password = usuario.getPassword(); // Obtiene el password codificado del usuario desde la base de datos
        /*String role = usuario.getRoles(); // Obtiene el rol del usuario desde la base de datos*/

        List<String> roles = Arrays.asList(usuario.getRoles().split(","));; // Obtiene la lista de roles del usuario desde la base de datos

        // Convierte la lista de roles en un arreglo de cadenas
        String[] rolesArray = roles.toArray(new String[0]);

        return User.withUsername(usuario.getUsername())
                .password(password) // Utiliza el password codificado desde la base de datos
                .roles(rolesArray) // Asigna el rol del usuario
                .build();
    }
}
