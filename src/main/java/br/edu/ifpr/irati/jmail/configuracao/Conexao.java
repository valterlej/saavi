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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Representa os parâmetros de uma conexão.
 *
 * @author Valter Estevam
 */
public class Conexao {
    
    /**
     * Relação de propriedades de conexão utilizadas pela JavaMail para 
     * estabelecer conexão com um servidor de e-mail.
     */
    private List<Propriedade> propriedades;
    
    /**
     * Dados para autenticação em conta de e-mail (usuário e senha)
     */
    private Autenticacao autenticacao;
        
    public Conexao() {
        propriedades = new ArrayList<>();
        autenticacao = new Autenticacao();
    }

    public Conexao(List<Propriedade> propriedades, Autenticacao autenticacao) {
        this.propriedades = propriedades;
        this.autenticacao = autenticacao;
    }
       
    public void adicionarPropriedade(Propriedade propriedade){
        this.propriedades.add(propriedade);
    }
    
    
    public void removerPropriedade(Propriedade propriedade){
        this.propriedades.remove(propriedade);
    }
    
    /**
     * @return the propriedades
     */
    public List<Propriedade> getPropriedades() {
        return propriedades;
    }

    /**
     * @param propriedades the propriedades to set
     */
    public void setPropriedades(List<Propriedade> propriedades) {
        this.propriedades = propriedades;
    }

    /**
     * @return the autenticacao
     */
    public Autenticacao getAutenticacao() {
        return autenticacao;
    }

    /**
     * @param autenticacao the autenticacao to set
     */
    public void setAutenticacao(Autenticacao autenticacao) {
        this.autenticacao = autenticacao;
    }
                       
}