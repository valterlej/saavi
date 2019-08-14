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

import br.edu.ifpr.irati.modelo.Administrador;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Categoria;
import br.edu.ifpr.irati.modelo.Entrevistado;
import br.edu.ifpr.irati.modelo.Questao;
import br.edu.ifpr.irati.modelo.QuestaoAberta;
import br.edu.ifpr.irati.modelo.QuestaoMultiplaEscolha;
import br.edu.ifpr.irati.modelo.Questionario;

/**
 * Criador de entidades para uso com o CRUD genérico.
 *
 * @author Valter Estevam
 */
public class EntidadeFactory {
    
    /**
     * Dada a classe informada retorna uma instância.
     * @param classe classe desejada.
     * @return objeto da classe desejada.
     */
    public static Object getObject(Class classe){
        
        if (classe == Campus.class){
            return new Campus();
        }else if (classe == Categoria.class){
            return new Categoria();
        }else if (classe == Entrevistado.class){
            return new Entrevistado();
        }else if (classe == Administrador.class){
            return new Administrador();
        }else if (classe == Questionario.class){
            return new Questionario();
        }else if (classe == QuestaoAberta.class){
            return new QuestaoAberta();
        }else if (classe == QuestaoMultiplaEscolha.class){
            return new QuestaoMultiplaEscolha();
        }else if (classe == Questao.class){
            return new Questao();
        }
        else{
            return new Object();
        }
        
    }    
}