package com.neohorizon.api.service.usuario;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.neohorizon.api.dto.usuario.UsuarioDTO;
import com.neohorizon.api.entity.usuario.Usuario;
import com.neohorizon.api.enums.RoleType;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.UsuarioMapper;
import com.neohorizon.api.repository.usuario.UsuarioRepository;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setUsuario_id(1L);
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("senhaCriptografada");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsuario_id(1L);
        usuarioDTO.setEmail("teste@teste.com");
        usuarioDTO.setSenha("123456");
        usuarioDTO.setCargo(RoleType.MANAGER);
    }


    @Test
    void testGetAllUsers() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioMapper.toDTOList(anyList())).thenReturn(List.of(usuarioDTO));

        List<UsuarioDTO> result = usuarioService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("teste@teste.com", result.get(0).getEmail());
    }


    @Test
    void testGetUserById_success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        UsuarioDTO result = usuarioService.getUserById(1L);

        assertEquals("teste@teste.com", result.getEmail());
    }

    @Test
    void testGetUserById_notFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> usuarioService.getUserById(1L));
    }


    @Test
    void testCreateUser_success() {
        when(usuarioMapper.toEntity(usuarioDTO)).thenReturn(usuario);
        when(passwordEncoder.encode("123456")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any())).thenReturn(usuario);
        when(usuarioMapper.toDTO(any())).thenReturn(usuarioDTO);

        UsuarioDTO result = usuarioService.createUser(usuarioDTO);

        assertEquals("teste@teste.com", result.getEmail());
        verify(passwordEncoder).encode("123456");
    }


    @Test
    void testUpdateUser_success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toEntity(usuarioDTO)).thenReturn(usuario);
        when(passwordEncoder.encode("123456")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        UsuarioDTO result = usuarioService.updateUser(1L, usuarioDTO);

        assertEquals("teste@teste.com", result.getEmail());
    }

    @Test
    void testUpdateUser_notFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> usuarioService.updateUser(1L, usuarioDTO));
    }


    @Test
    void testUpdatePassword_success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("novaSenha")).thenReturn("senhaNovaCripto");

        boolean result = usuarioService.updatePassword(1L, "novaSenha");

        assertTrue(result);
        verify(usuarioRepository).save(usuario);
        assertEquals("senhaNovaCripto", usuario.getSenha());
    }

    @Test
    void testUpdatePassword_userNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> usuarioService.updatePassword(1L, "123"));
    }


    @Test
    void testDeleteUser_success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.deleteUser(1L);

        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void testDeleteUser_notFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> usuarioService.deleteUser(1L));
    }


    @Test
    void testFindById_success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findById(1L);

        assertEquals("teste@teste.com", result.getEmail());
    }

    @Test
    void testFindById_notFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> usuarioService.findById(1L));
    }


    @Test
    void testLoadUserById_success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UserDetails details = usuarioService.loadUserById(1L);

        assertEquals("teste@teste.com", details.getUsername());
    }

    @Test
    void testLoadUserById_notFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> usuarioService.loadUserById(1L));
    }


    @Test
    void testAuthenticate_success() {
        when(usuarioRepository.findByEmail("teste@teste.com"))
            .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("123456", "senhaCriptografada"))
            .thenReturn(true);

        try (MockedStatic<com.neohorizon.api.config.JwtUtils> jwtMock = Mockito.mockStatic(com.neohorizon.api.config.JwtUtils.class)) {
            jwtMock.when(() -> com.neohorizon.api.config.JwtUtils.generateToken(any()))
                .thenReturn("TOKENTESTE");

            String token = usuarioService.authenticate("teste@teste.com", "123456");

            assertEquals("TOKENTESTE", token);
        }
    }

    @Test
    void testAuthenticate_invalidPassword() {
        when(usuarioRepository.findByEmail("teste@teste.com"))
            .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("errada", "senhaCriptografada"))
            .thenReturn(false);

        assertThrows(UsernameNotFoundException.class,
                () -> usuarioService.authenticate("teste@teste.com", "errada"));
    }

    @Test
    void testAuthenticate_userNotFound() {
        when(usuarioRepository.findByEmail("x@x.com"))
            .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> usuarioService.authenticate("x@x.com", "123"));
    }
}
