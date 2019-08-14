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
package br.edu.ifpr.irati.configuracao.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Armazena os dados necessários para acesso e autenticação à conta de e-mail 
 * utilizada para envio das mensagens automáticas.
 * 
 * @author Valter Estevam
 */
@Entity (name="autenticacaoemail")
public class AutenticacaoEmail implements Serializable{
    
    /**
     * Código identificador do registro criado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idAutenticacao;
    
    /**
     * Conta do usuário no servidor de e-mail que será utilizada para envio das mensagens.
     */
    @NotNull(message = "Endereço do usuário é obrigatório.")
    @Size(min=1, max=150, message = "O usuário deve possuir entre 1 e 150 caracteres.")    
    @Column(name = "usuario", nullable = false, length = 150)
    private String usuario;
    
    /**
     * Senha para liberação do acesso à conta de envio de e-mails.
     */
    @NotNull(message = "Senha do usuário é obrigatória.")
    @Size(min=1, max=100, message = "A senha deve possuir entre 1 e 100 caracteres.")    
    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    /**
     * Construtor padrão.
     */
    public AutenticacaoEmail() {
        idAutenticacao = 0;
        usuario = "";
        senha = "";
    }

    /**
     * 
     * @param idAutenticacao identificador do registro.
     * @param usuario conta de e-mail.
     * @param senha  senha para acesso à conta de e-mail.
     */
    public AutenticacaoEmail(int idAutenticacao, String usuario, String senha) {
        this.idAutenticacao = idAutenticacao;
        this.usuario = usuario;
        this.senha = senha;
    }
        
    /**
     * 
     * @return identificador do registro.
     */
    public int getIdAutenticacao() {
        return idAutenticacao;
    }

    /**
     * 
     * @param idAutenticacao identificador do registro.
     */
    public void setIdAutenticacao(int idAutenticacao) {
        this.idAutenticacao = idAutenticacao;
    }

    /**
     * 
     * @return conta de e-mail para envio de mensagens.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * 
     * @param usuario conta de e-mail para envio de mensagens.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * 
     * @return senha para acesso à conta de e-mail.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * 
     * @param senha senha para acesso à conta de e-mail.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}