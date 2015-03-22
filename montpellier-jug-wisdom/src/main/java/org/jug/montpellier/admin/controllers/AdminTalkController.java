/*
 * #%L
 * Wisdom-Framework
 * %%
 * Copyright (C) 2013 - 2014 Wisdom Framework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.jug.montpellier.admin.controllers;

import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.core.api.CartridgeSupport;
import org.jug.montpellier.core.controller.JugController;
import org.jug.montpellier.forms.apis.PropertySheet;
import org.jug.montpellier.models.Speaker;
import org.jug.montpellier.models.Talk;
import org.montpellierjug.store.jooq.tables.daos.TalkDao;
import org.wisdom.api.annotations.*;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@Controller
@Path("/admin/talk")
public class AdminTalkController extends JugController {

    @View("admin")
    Template template;

    @Requires
    PropertySheet propertySheet;

    @Requires
    TalkDao talkDao;

    public AdminTalkController(@Requires CartridgeSupport cartridgeSupport) {
        super(cartridgeSupport);
    }

    @Route(method = HttpMethod.GET, uri = "/")
    public Result home() {
        return template(template).render();
    }


    @Route(method = HttpMethod.GET, uri = "/{id}")
    public Result speaker(@Parameter("id") Long id) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        Talk editedTalk = Talk.build(talkDao.findById(id));
        return template(template).withPropertySheet(propertySheet.getRenderable(this, editedTalk)).render();

    }

    @Route(method = HttpMethod.POST, uri = "/{id}")
    public Result saveSpeaker(@Parameter("id") Long id, @Body Talk talk) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        talkDao.update(talk.into(new org.montpellierjug.store.jooq.tables.pojos.Talk()));
        return redirect("/admin/talk/" + id);
    }

}
