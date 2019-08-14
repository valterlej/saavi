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
 *
 * Armazena o nome e o valor de uma propriedade utilizada para fazer a
 * conexão com o servidor de envio de e-mails automáticos.
 * 
 * @author Valter Estevam
 */
@Entity (name="propriedadeemail")
public class PropriedadeEmail implements Serializable{
    
    /**
     * Identificador do registro criado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idPropriedade;
    
    /**
     * Nome da propriedade.
     */
    @NotNull(message = "Nome é obrigatório")
    @Size(min=1, max=100, message = "O nome deve possuir entre 1 e 100 caracteres.") 
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    /**
     * Valor da propriedade.
     */
    @NotNull(message = "Valor é obrigatório")
    @Size(min=0, max=100, message = "O valor deve possuir entre 0 e 100 caracteres.") 
    @Column(name = "valor", nullable = true, length = 100)
    private String valor;

    /**
     * Construtor padrão.
     */
    public PropriedadeEmail() {
        idPropriedade = 0;
        nome = "";
        valor = "";
    }

    /**
     * 
     * @param idPropriedade identificador único da propriedade.
     * @param nome nome da propriedade.
     * @param valor valor da propriedade.
     */
    public PropriedadeEmail(int idPropriedade, String nome, String valor) {
        this.idPropriedade = idPropriedade;
        this.nome = nome;
        this.valor = valor;
    }
            
    /**
     * 
     * @return identificador único da propriedade.
     */
    public int getIdPropriedade() {
        return idPropriedade;
    }

    /**
     * 
     * @param idPropriedade identificador único da propriedade.
     */
    public void setIdPropriedade(int idPropriedade) {
        this.idPropriedade = idPropriedade;
    }

    /**
     * 
     * @return nome da propriedade.
     */
    public String getNome() {
        return nome;
    }

    /**
     * 
     * @param nome nome da propriedade.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * 
     * @return valor da propriedade.
     */
    public String getValor() {
        return valor;
    }

    /**
     * 
     * @param valor valor da propriedade.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
    
}