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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;

/**
 *
 * Entrevistado
 * 
 * @author Valter Estevam
 */
@Entity (name="entrevistado")
public class Entrevistado implements Serializable{
    
    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idEntrevistado;
    
    /**
     * Nome completo do entrevistado.
     */
    @NotNull(message="Nome é obrigatório.")
    @Size(min=1, max=100, message = "O nome deve possuir entre 1 e 100 caracteres.")
    @Column(name = "nomecompleto", nullable = false, length = 100)
    private String nomeCompleto;
    
    /**
     * Data de nascimento
     */
    @NotNull(message = "Data de nascimento é obrigatória.")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    
    /**
     * Cadastro de pessoa física.
     * 
     * O armazenamento é feito em SHA-256.
     * 
     */
    @Column(name = "cpf", nullable = false, length = 100, unique = true)
    private String cpf; //será armazenado um resumo do cpf
    
    /**
     * Endereço de e-mail.
     */
    @NotNull(message = "E-mail é obrigatório.")
    @Size(min=1, max = 150, message = "O e-mail deve possuir entre 1 e 150 caracteres.")
    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;
        
    /**
     * Senha para acesso.
     * 
     * O armazenamento é feito em SHA-256.
     * 
     */
    @Column(name = "senha", nullable = false, length = 100)
    private String senha; // será armazenado um resumo da senha
    
    /**
     * Campus ao qual o entrevistado está associado.
     */
    @OneToOne
    private Campus campus;
    
    /**
     * Categoria à qual o entrevistado está associado.
     */
    @OneToOne
    private Categoria categoria;
    
    /**
     * Validado?
     * 
     * Inicialmente o campo marca que não.
     */
    @Type(type = "yes_no")
    private boolean validado;

    /**
     * Construtor padrão.
     */
    public Entrevistado() {    
        idEntrevistado = 0;
        nomeCompleto = "";
        cpf = "";
        email = "";
        senha = "";
        dataNascimento = new Date();
        campus = new Campus();
        categoria = new Categoria();    
        validado = false;
    }

    /**
     * Construtor.
     * 
     * @param idEntrevistado identificador único.
     * @param nomeCompleto nome completo.
     * @param dataNascimento data de nascimento.
     * @param cpf cadastro de pessoa física.
     * @param email endereço de e-mail.
     * @param senha senha para acesso.
     * @param campus campus de vínculo.
     * @param categoria categoria de vínculo.
     * @param validado validado.
     */
    public Entrevistado(int idEntrevistado, String nomeCompleto, Date dataNascimento, String cpf, String email, String senha, Campus campus, Categoria categoria, boolean validado) {
        this.idEntrevistado = idEntrevistado;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.campus = campus;
        this.categoria = categoria;
        this.validado = validado;
    }

    /**
     * 
     * @return identificador único.
     */
    public int getIdEntrevistado() {
        return idEntrevistado;
    }

    /**
     * 
     * @param idEntrevistado identificador único.
     */
    public void setIdEntrevistado(int idEntrevistado) {
        this.idEntrevistado = idEntrevistado;
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
     * @return cadastro de pessoa física.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * 
     * @param cpf cadastro de pessoa física.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    /**
     * 
     * @return campus de vínculo.
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * 
     * @param campus campus de vínculo.
     */
    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    /**
     * 
     * @return categoria de vínculo.
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * 
     * @param categoria categoria de vínculo.
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * 
     * @return data de nascimento.
     */
    public Date getDataNascimento() {
        return dataNascimento;
    }

    /**
     * 
     * @param dataNascimento data de nascimento.
     */
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    /**
     * 
     * @return true se está validado.
     */
    public boolean isValidado() {
        return validado;
    }

    /**
     * 
     * @param validado true se está validado.
     */
    public void setValidado(boolean validado) {
        this.validado = validado;
    }
        
}