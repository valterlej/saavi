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
package br.edu.ifpr.irati.modelo.relatorio;

/**
 *
 * Registro em uma tabela do relatório.
 * 
 * @author Valter Estevam
 */
public abstract class Registro {
    
    /**
     * Título do registro.
     * 
     * Equivale à primeira coluna.
     */
    protected String titulo;
        
    /**
     * Construtor padrão.
     */
    protected Registro() {
        titulo = "";        
    }
    
    /**
     * Construtor.
     * 
     * @param titulo titulo do registro.
     */
    public Registro(String titulo) {
        this.titulo = titulo;
    }
     
    /**
     * 
     * @return título do registro.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * 
     * @param titulo título do registro.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
                     
}