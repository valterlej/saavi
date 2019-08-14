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

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * Classe para retornar o caminho absoluto do projeto no sistema de arquivos do servidor web.
 * 
 * @author Valter Estevam
 */
public class Path {
    
    /**
     * Obter o caminho do projeto no servidor de arquivos.
     * 
     * @return string com o caminho.
     */
    public static String obterCaminhoServidor(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String pathRel = servletContext.getRealPath(".");
        pathRel = pathRel.substring(0,pathRel.length()-1);
        return pathRel; 
    }
    
}