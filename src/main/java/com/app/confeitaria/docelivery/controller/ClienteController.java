package com.app.confeitaria.docelivery.controller;

import com.app.confeitaria.docelivery.dto.AtualizarPerfilDTO;
import com.app.confeitaria.docelivery.dto.ClientePerfilDTO;
import com.app.confeitaria.docelivery.model.entity.Cliente;
import com.app.confeitaria.docelivery.model.entity.Usuario;
import com.app.confeitaria.docelivery.model.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cliente")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.upload.dir:C:/docelivery-storage/}")
    private String uploadDir;

    @Value("${app.base-url:http://10.0.2.2:8080}")
    private String baseUrl;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // ── DIAGNÓSTICO: extrai Cliente do principal autenticado ─────────────────
    private Cliente clienteAutenticado(Object principal) {
        if (principal instanceof Usuario usuario) {
            System.out.println("LOG PERFIL: Usuário autenticado: " + usuario.getEmail()
                    + " | Role: " + usuario.getAuthorities()
                    + " | Tipo: " + usuario.getTipoUsuario());

            return clienteRepository.findById(usuario.getId())
                    .orElseThrow(() -> {
                        System.out.println("LOG PERFIL: ID " + usuario.getId() + " não encontrado como CLIENTE");
                        return new RuntimeException("Cliente não encontrado.");
                    });
        }
        System.out.println("LOG PERFIL: Principal não é instância de Usuario: "
                + (principal != null ? principal.getClass().getName() : "null"));
        throw new RuntimeException("Usuário não autenticado.");
    }

    // ════════════════════════════════════════════════════════════════════════
    // GET /api/cliente/perfil
    // ════════════════════════════════════════════════════════════════════════
    @GetMapping("/perfil")
    public ResponseEntity<ClientePerfilDTO> getPerfil(@AuthenticationPrincipal Object principal) {
        System.out.println("LOG PERFIL: GET /api/cliente/perfil chamado");
        Cliente cliente = clienteAutenticado(principal);
        System.out.println("LOG CPF       : " + cliente.getCpf());
        System.out.println("LOG DATA_NASC : " + cliente.getDataNascimento());
        System.out.println("LOG APELIDO   : " + cliente.getApelido());
        System.out.println("LOG FOTO      : " + cliente.getFotoPerfil());
        System.out.println("LOG PREF      : " + cliente.getPreferencias());
        ClientePerfilDTO dto = ClientePerfilDTO.fromCliente(cliente);
        System.out.println("LOG DTO CPF   : " + dto.getCpf());
        System.out.println("LOG DTO DATA  : " + dto.getDataNascimento());
        try {
            System.out.println("LOG DTO JSON  : " + new com.fasterxml.jackson.databind.ObjectMapper()
                .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
                .writeValueAsString(dto));
        } catch (Exception e) {
            System.out.println("LOG DTO JSON ERRO: " + e.getMessage());
        }
        return ResponseEntity.ok(dto);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PUT /api/cliente/perfil
    // ════════════════════════════════════════════════════════════════════════
    @PutMapping("/perfil")
    @Transactional
    public ResponseEntity<ClientePerfilDTO> atualizarPerfil(
            @AuthenticationPrincipal Object principal,
            @RequestBody AtualizarPerfilDTO dados) {

        System.out.println("LOG PERFIL: PUT /api/cliente/perfil chamado");
        Cliente cliente = clienteAutenticado(principal);

        // Atualiza apenas campos não-nulos
        if (dados.getNome() != null && !dados.getNome().isBlank())
            cliente.setNome(dados.getNome());

        if (dados.getApelido() != null)
            cliente.setApelido(dados.getApelido());

        if (dados.getEmail() != null && !dados.getEmail().isBlank())
            cliente.setEmail(dados.getEmail());

        if (dados.getTelefone() != null)
            cliente.setTelefone(dados.getTelefone());

        if (dados.getCep() != null)
            cliente.setCep(dados.getCep());

        if (dados.getLogradouro() != null)
            cliente.setEndereco(dados.getLogradouro()); // entidade usa "endereco"

        if (dados.getNumero() != null)
            cliente.setNumero(dados.getNumero());

        if (dados.getComplemento() != null)
            cliente.setComplemento(dados.getComplemento());

        if (dados.getBairro() != null)
            cliente.setBairro(dados.getBairro());

        if (dados.getCidade() != null)
            cliente.setCidade(dados.getCidade());

        if (dados.getEstado() != null)
            cliente.setUf(dados.getEstado()); // entidade usa "uf"

        if (dados.getFotoPerfil() != null)
            cliente.setFotoPerfil(dados.getFotoPerfil());

        if (dados.getDataNascimento() != null && !dados.getDataNascimento().isBlank()) {
            try {
                cliente.setDataNascimento(LocalDate.parse(dados.getDataNascimento()));
            } catch (Exception e) {
                System.out.println("LOG PERFIL: Data inválida ignorada: " + dados.getDataNascimento());
            }
        }

        // Serializa listas como JSON string
        try {
            if (dados.getPreferencias() != null)
                cliente.setPreferencias(objectMapper.writeValueAsString(dados.getPreferencias()));
            if (dados.getRestricoes() != null)
                cliente.setRestricoes(objectMapper.writeValueAsString(dados.getRestricoes()));
        } catch (Exception e) {
            System.out.println("LOG PERFIL: Erro ao serializar preferências/restrições: " + e.getMessage());
        }

        // CPF NÃO é atualizado aqui — intencional
        Cliente salvo = clienteRepository.save(cliente);
        System.out.println("LOG PERFIL: Perfil salvo com sucesso para: " + salvo.getEmail());
        return ResponseEntity.ok(ClientePerfilDTO.fromCliente(salvo));
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/cliente/foto
    // ════════════════════════════════════════════════════════════════════════
    @PostMapping("/foto")
    @Transactional
    public ResponseEntity<?> uploadFoto(
            @AuthenticationPrincipal Object principal,
            @RequestParam("foto") MultipartFile foto) {

        System.out.println("LOG PERFIL: POST /api/cliente/foto chamado");

        if (foto == null || foto.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nenhum arquivo enviado."));
        }

        Cliente cliente = clienteAutenticado(principal);

        try {
            // Garante que a pasta existe
            Path pastaFotos = Paths.get(uploadDir, "fotos");
            Files.createDirectories(pastaFotos);

            // Nome único para o arquivo
            String ext = obterExtensao(foto.getOriginalFilename());
            String filename = "perfil_" + cliente.getId() + "_" + System.currentTimeMillis() + ext;
            Path destino = pastaFotos.resolve(filename);

            // Salva o arquivo
            foto.transferTo(destino);

            // URL pública
            String fotoUrl = "/uploads/fotos/" + filename;

            // Atualiza banco
            cliente.setFotoPerfil(fotoUrl);
            clienteRepository.save(cliente);

            System.out.println("LOG PERFIL: Foto salva em: " + destino + " | URL: " + fotoUrl);
            return ResponseEntity.ok(Map.of("fotoUrl", fotoUrl));

        } catch (IOException e) {
            System.out.println("LOG PERFIL: Erro ao salvar foto: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao salvar foto: " + e.getMessage()));
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // PUT /api/cliente/atualizar/{id}  — mantido para compatibilidade
    // ════════════════════════════════════════════════════════════════════════
    @PutMapping("/atualizar/{id}")
    @Transactional
    @Deprecated
    public ResponseEntity<?> atualizarPorId(@PathVariable Long id, @RequestBody Cliente dados) {
        System.out.println("LOG: atualizar/{id} chamado (legado) para ID: " + id);
        return clienteRepository.findById(id).map(c -> {
            if (dados.getNome() != null) c.setNome(dados.getNome());
            if (dados.getTelefone() != null) c.setTelefone(dados.getTelefone());
            if (dados.getEmail() != null) c.setEmail(dados.getEmail());
            if (dados.getCep() != null) c.setCep(dados.getCep());
            if (dados.getEndereco() != null) c.setEndereco(dados.getEndereco());
            if (dados.getBairro() != null) c.setBairro(dados.getBairro());
            if (dados.getCidade() != null) c.setCidade(dados.getCidade());
            if (dados.getUf() != null) c.setUf(dados.getUf());
            if (dados.getApelido() != null) c.setApelido(dados.getApelido());
            return ResponseEntity.ok(clienteRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    private String obterExtensao(String filename) {
        if (filename == null || !filename.contains(".")) return ".jpg";
        return filename.substring(filename.lastIndexOf('.'));
    }
}
