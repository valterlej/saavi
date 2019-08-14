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
package br.edu.ifpr.irati.jmail.configuracao;

/**
 * Representa uma propriedade de conexão utilizada pela JavaMail para
 * realizar uma conexão e o envio de um e-mail a partir de um servidor.
 * 
 * @author Valter Estevam
 */
public class Propriedade {
    
    /**
     * Nome da propriedade.
     */
    private String nome;
    
    /**
     * Valor da propriedade.
     */
    private String valor;

    /**
     * Construtor padrão.
     */
    public Propriedade() {
        nome = "";
        valor = "";
    }

    /**
     * Construtor completo.
     * 
     * @param nome
     * @param valor 
     */
    public Propriedade(String nome, String valor) {
        this.nome = nome;
        this.valor = valor;
    }   
    
    
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
        
}