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
 * Configurações gerais do sistema.
 * 
 * @author Valter Estevam
 */
@Entity (name="configuracao")
public class ConfiguracaoWeb implements Serializable {
    
    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idConfiguracao;
    
    /**
     * Nome da instituição.
     */
    @NotNull(message = "Nome da instituição é obrigatório.")
    @Size(min=1, max=150, message = "O nome da instituição deve possuir entre 1 e 150 caracteres.")
    @Column(name = "nomeinstituicao", nullable = false, length = 150)
    private String nomeInstituicao;
    
    /**
     * Endereço da Instituição.
     */
    @Size(min=1, max=150, message = "O endereço da instituição deve possuir entre 1 e 150 caracteres.")
    @Column(name = "enderecoinstituicao", nullable = true, length = 150)
    private String enderecoInstituicao;
    
    /**
     * Telefone da instituição.
     */
    @Size(min=1, max=50, message = "O telefone deve possuir entre 1 e 50 caracteres.")
    @Column(name = "telefoneinstituicao", nullable = true, length = 50)
    private String telefoneInstituicao;
    
    /**
     * E-mail para contato. Preferencialmente de um responsável pela CPA.
     */
    @NotNull(message = "E-mail da instituição é obrigatório.")
    @Size(min=1, max=150, message = "O e-mail da instituição deve possuir entre 1 e 150 caracteres.")
    @Column(name = "emailcontato", nullable = false, length = 150)
    private String emailContato;
       
    /**
     * Endereço do servidor web onde sistema foi implantado.
     */
    @NotNull(message = "Endereço do servidor é obrigatório.")
    @Size(min=1, max=150, message = "O endereço do servidor deve possuir entre 1 e 150 caracteres.")
    @Column(name = "enderecoservidor", nullable = false, length = 150)
    private String enderecoServidor;
    
    /**
     * Construtor padrão.
     */
    public ConfiguracaoWeb() {
        idConfiguracao = 0;
        nomeInstituicao = "";
        enderecoInstituicao = "";
        telefoneInstituicao = "";
        emailContato = "";
        enderecoServidor = "";
    }

    /**
     * Construtor.
     * 
     * @param idConfiguracao identificador único.
     * @param nomeInstituicao nome da instituição.
     * @param enderecoInstituicao endereço da instituição.
     * @param telefoneInstituicao telefone da instituição.
     * @param emailContato e-mail para contato.
     * @param enderecoServidor endereço do servidor.
     */
    public ConfiguracaoWeb(int idConfiguracao, String nomeInstituicao, String enderecoInstituicao, String telefoneInstituicao, String emailContato, String enderecoServidor) {
        this.idConfiguracao = idConfiguracao;
        this.nomeInstituicao = nomeInstituicao;
        this.enderecoInstituicao = enderecoInstituicao;
        this.telefoneInstituicao = telefoneInstituicao;
        this.emailContato = emailContato;
        this.enderecoServidor = enderecoServidor;
    }

    /**
     * 
     * @return identificador único.
     */            
    public int getIdConfiguracao() {
        return idConfiguracao;
    }

    /**
     * 
     * @param idConfiguracao identificador único.
     */
    public void setIdConfiguracao(int idConfiguracao) {
        this.idConfiguracao = idConfiguracao;
    }

    /**
     * 
     * @return nome da instituição.
     */
    public String getNomeInstituicao() {
        return nomeInstituicao;
    }

    /**
     * 
     * @param nomeInstituicao nome da instituição.
     */
    public void setNomeInstituicao(String nomeInstituicao) {
        this.nomeInstituicao = nomeInstituicao;
    }

    /**
     * 
     * @return endereço da instituição.
     */
    public String getEnderecoInstituicao() {
        return enderecoInstituicao;
    }

    /**
     * 
     * @param enderecoInstituicao endereço da instituição.
     */
    public void setEnderecoInstituicao(String enderecoInstituicao) {
        this.enderecoInstituicao = enderecoInstituicao;
    }

    /**
     * 
     * @return telefone da instituição.
     */
    public String getTelefoneInstituicao() {
        return telefoneInstituicao;
    }

    /**
     * 
     * @param telefoneInstituicao telefone da instituição.
     */
    public void setTelefoneInstituicao(String telefoneInstituicao) {
        this.telefoneInstituicao = telefoneInstituicao;
    }

    /**
     * 
     * @return e-mail para contato.
     */
    public String getEmailContato() {
        return emailContato;
    }

    /**
     * 
     * @param emailContato e-mail para contato.
     */
    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }
    
    /**
     * 
     * @return endereço do servidor onde o sistema foi implantado.
     */
    public String getEnderecoServidor() {
        return enderecoServidor;
    }

    /**
     * 
     * @param endereçoServidor endereço do servidor onde o sistema foi implantado.
     */
    public void setEnderecoServidor(String endereçoServidor) {
        this.enderecoServidor = endereçoServidor;
    }
                       
}