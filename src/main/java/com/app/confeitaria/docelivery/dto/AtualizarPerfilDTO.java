package com.app.confeitaria.docelivery.dto;

import java.util.List;

public class AtualizarPerfilDTO {
    private String nome;
    private String apelido;
    private String dataNascimento; // YYYY-MM-DD — convertido para LocalDate no service
    private String email;
    private String telefone;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String fotoPerfil;
    private List<String> preferencias;
    private List<String> restricoes;

    // Getters e Setters
    public String getNome()                    { return nome; }
    public void setNome(String nome)           { this.nome = nome; }

    public String getApelido()                 { return apelido; }
    public void setApelido(String apelido)     { this.apelido = apelido; }

    public String getDataNascimento()          { return dataNascimento; }
    public void setDataNascimento(String d)    { this.dataNascimento = d; }

    public String getEmail()                   { return email; }
    public void setEmail(String email)         { this.email = email; }

    public String getTelefone()                { return telefone; }
    public void setTelefone(String telefone)   { this.telefone = telefone; }

    public String getCep()                     { return cep; }
    public void setCep(String cep)             { this.cep = cep; }

    public String getLogradouro()              { return logradouro; }
    public void setLogradouro(String l)        { this.logradouro = l; }

    public String getNumero()                  { return numero; }
    public void setNumero(String numero)       { this.numero = numero; }

    public String getComplemento()             { return complemento; }
    public void setComplemento(String c)       { this.complemento = c; }

    public String getBairro()                  { return bairro; }
    public void setBairro(String bairro)       { this.bairro = bairro; }

    public String getCidade()                  { return cidade; }
    public void setCidade(String cidade)       { this.cidade = cidade; }

    public String getEstado()                  { return estado; }
    public void setEstado(String estado)       { this.estado = estado; }

    public String getFotoPerfil()              { return fotoPerfil; }
    public void setFotoPerfil(String f)        { this.fotoPerfil = f; }

    public List<String> getPreferencias()      { return preferencias; }
    public void setPreferencias(List<String> p){ this.preferencias = p; }

    public List<String> getRestricoes()        { return restricoes; }
    public void setRestricoes(List<String> r)  { this.restricoes = r; }
}
