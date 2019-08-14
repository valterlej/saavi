/*
 * Copyright 2019 Valter Luís Estevam Junior
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 */
package br.edu.ifpr.irati.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * Administrador do sistema.
 * 
 * @author Valter Estevam
 */
@Entity (name="administrador")
public class Administrador implements Serializable{
    
    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idAdministrador;
    
    /**
     * Nome completo do administrador.
     */
    @NotNull(message="Nome é obrigatório.")
    @Size(min=1, max=100, message = "O nome deve possuir entre 1 e 100 caracteres.")
    @Column(name = "nomecompleto", nullable = false, length = 100)
    private String nomeCompleto;
    
    /**
     * Endereço de e-mail.
     */
    @NotNull(message = "E-mail é obrigatório.")
    @Size(min=1, max = 150, message = "O e-mail deve possuir entre 1 e 150 caracteres.")
    @Column(name = "email", nullable = false, length = 150)
    private String email;
    
    /**
     * Senha para acesso ao sistema.
     */
    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    /**
     * Construtor padrão.
     */
    public Administrador() {        
        idAdministrador = 0;        
        nomeCompleto = "";        
        email = "";        
        senha = "";        
    }

    /**
     * Construtor.
     * 
     * @param idAdministrador identificador único.
     * @param nomeCompleto nome completo.
     * @param email endereço de e-mail.
     * @param senha senha para acesso.
     */
    public Administrador(int idAdministrador, String nomeCompleto, String email, String senha) {
        this.idAdministrador = idAdministrador;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
    }
        
    /**
     * 
     * @return identificador único.
     */
    public int getIdAdministrador() {
        return idAdministrador;
    }

    /**
     * 
     * @param idAdministrador identificador único.
     */
    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    /**
     * 
     * @return nome completo.
     */
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    /**
     * 
     * @param nomeCompleto nome completo.
     */
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    /**
     * 
     * @return endereço de e-mail.
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email endereço de e-mail.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return senha para acesso.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * 
     * @param senha senha para acesso.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}