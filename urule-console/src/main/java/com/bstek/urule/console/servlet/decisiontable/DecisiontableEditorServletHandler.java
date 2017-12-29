/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.urule.console.servlet.decisiontable;

import com.bstek.urule.console.servlet.BaseServletHandler;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Jacky.gao
 * @since 2016年8月1日
 */
public class DecisiontableEditorServletHandler extends BaseServletHandler {
    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        VelocityContext context = new VelocityContext();
        context.put("contextPath", req.getContextPath());
        String file = req.getParameter("file");
        String project = buildProjectNameFromFile(file);
        if (project != null) {
            context.put("project", project);
        }
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        Template template = ve.getTemplate("html/decisiontable-editor.html", "utf-8");
        PrintWriter writer = resp.getWriter();
        template.merge(context, writer);
        writer.close();
    }

    @Override
    public String url() {
        return "/decisiontableeditor";
    }
}
