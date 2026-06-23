package com.app.confeitaria.docelivery.dto;

import com.app.confeitaria.docelivery.model.entity.Cliente;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class ClientePerfilDTO {

    private Long id;
    private String nome;
    private String apelido;
    private String cpf;
    private String dataNascimento; // YYYY-MM-DD
    private String email;
    private String telefone;
    private String cep;
    private String logradouro;     // mapeado de "endereco" na entidade
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;         // mapeado de "uf" na entidade
    private String fotoPerfil;
    private List<String> preferencias;
    private List<String> restricoes;

    private static final ObjectMapper mapper = new ObjectMapper();

    // Construtor privado — usar fromCliente()
    private ClientePerfilDTO() {}

    /** Converte entidade Cliente → DTO de resposta */
    public static ClientePerfilDTO fromCliente(Cliente c) {
        ClientePerfilDTO dto = new ClientePerfilDTO();
        dto.id           = c.getId();
        dto.nome         = c.getNome();
        dto.apelido      = c.getApelido();
        dto.cpf          = c.getCpf();
        dto.email        = c.getEmail();
        dto.telefone     = c.getTelefone();
        dto.cep          = c.getCep();
        dto.logradouro   = c.getEndereco();   // campo da entidade é "endereco"
        dto.numero       = c.getNumero();
        dto.complemento  = c.getComplemento();
        dto.bairro       = c.getBairro();
        dto.cidade       = c.getCidade();
        dto.estado       = c.getUf();         // campo da entidade é "uf"
        dto.fotoPerfil   = c.getFotoPerfil();

        if (c.getDataNascimento() != null) {
            dto.dataNascimento = c.getDataNascimento().toString(); // LocalDate.toString() = YYYY-MM-DD
        }

        dto.preferencias = parseJsonList(c.getPreferencias());
        dto.restricoes   = parseJsonList(c.getRestricoes());
        return dto;
    }

    @SuppressWarnings("unchecked")
    private static List<String> parseJsonList(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return mapper.readValue(json, List.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public Long getId()                  { return id; }
    public String getNome()              { return nome; }
    public String getApelido()           { return apelido; }
    public String getCpf()               { return cpf; }
    public String getDataNascimento()    { return dataNascimento; }
    public String getEmail()             { return email; }
    public String getTelefone()          { return telefone; }
    public String getCep()               { return cep; }
    public String getLogradouro()        { return logradouro; }
    public String getNumero()            { return numero; }
    public String getComplemento()       { return complemento; }
    public String getBairro()            { return bairro; }
    public String getCidade()            { return cidade; }
    public String getEstado()            { return estado; }
    public String getFotoPerfil()        { return fotoPerfil; }
    public List<String> getPreferencias(){ return preferencias; }
    public List<String> getRestricoes()  { return restricoes; }
}
