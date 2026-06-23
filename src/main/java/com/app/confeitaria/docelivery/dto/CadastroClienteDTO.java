package com.app.confeitaria.docelivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CadastroClienteDTO(

        @NotBlank(message = "Nome é obrigatório.")
        String nome,

        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail inválido.")
        String email,

        @NotBlank(message = "CPF é obrigatório.")
        @Size(min = 11, max = 11, message = "CPF deve conter 11 dígitos.")
        String cpf,

        @NotNull(message = "Data de nascimento é obrigatória.")
        @Past(message = "Data de nascimento deve ser uma data passada.")
        LocalDate dataNascimento,

        @NotBlank(message = "Senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha,

        // Opcionais
        String sobrenome,
        String telefone,
        String apelido
) {}
