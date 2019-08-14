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
package br.edu.ifpr.irati.mb.util;

/**
 *
 * Organiza os parâmetros utilizados para controlar os 
 * elementos da interface gráfica default de gerenciamento de entidades.
 * 
 * @author Valter Estevam
 */
public class ParametrosViewCrud {
    
    /**
     * Informa se é uma nova entidade.
     */
    private boolean novo;
    
    /**
     * Informa se é para mostrar os campos em modo de edição.
     */
    private boolean mostrarEdicao;
    
    /**
     * Informa se é para mostrar a listagem de entidades.
     */
    private boolean mostrarListagem;
    
    /**
     * Informa qual é a página atual vinculada ao objeto controlador que contém estes parâmetros.
     */
    private String paginaAtual;
    
    /**
     * Informa para qual página o sistema deve retornar caso o botão cancelar seja clicado.
     */
    private String homePage;

    /**
     * Contrutor padrão.
     * 
     * @param paginaAtual
     * @param homePage 
     */
    public ParametrosViewCrud(String paginaAtual, String homePage) {        
        this.paginaAtual = paginaAtual;
        this.homePage = homePage;
        this.reset();
    }
    
    /**
     * Reiniciar os parâmetros da interface.
     */
    public void reset(){
        novo = true;
        mostrarEdicao = false;
        mostrarListagem = true;                
    }
    
    /**
     * Colocar a interface em modo de inserção.
     */
    public void modoInserir(){
        novo = true;
        mostrarEdicao = true;
        mostrarListagem = false;
    }
    
    /**
     * Colocar a interface em modo de alteração.
     */
    public void modoAlterar(){
        novo = false;
        mostrarEdicao = true;
        mostrarListagem = false;
    }

    /**
     * 
     * @return true se é um novo cadastro.
     */
    public boolean isNovo() {
        return novo;
    }

    /**
     * 
     * @param novo true se é um novo cadastro.
     */
    public void setNovo(boolean novo) {
        this.novo = novo;
    }

    /**
     * 
     * @return true se é para mostrar o painel de edição.
     */
    public boolean isMostrarEdicao() {
        return mostrarEdicao;
    }

    /**
     * 
     * @param mostrarEdicao true se é para mostrar o painel de edição.
     */
    public void setMostrarEdicao(boolean mostrarEdicao) {
        this.mostrarEdicao = mostrarEdicao;
    }

    /**
     * 
     * @return true se é para mostrar o painel de listagem
     */
    public boolean isMostrarListagem() {
        return mostrarListagem;
    }

    /**
     * 
     * @param mostrarListagem true se é para mostrar o painel de listagem
     */
    public void setMostrarListagem(boolean mostrarListagem) {
        this.mostrarListagem = mostrarListagem;
    }

    /**
     * 
     * @return página atual associada a estes parâmetros.
     */
    public String getPaginaAtual() {
        return paginaAtual;
    }

    /**
     * 
     * @param paginaAtual página atual associada a estes parâmetros.
     */
    public void setPaginaAtual(String paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

    /**
     * 
     * @return para para retorno em caso de cancelamento.
     */
    public String getHomePage() {
        return homePage;
    }
    
    /**
     * 
     * @param homePage página para retorno em caso de cancelamento.
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
      
}