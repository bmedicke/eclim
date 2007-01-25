/**
 * Copyright (c) 2005 - 2006
 *
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
 */
package org.eclim.command.xml.validate;

import java.util.ArrayList;
import java.util.List;

import org.eclim.command.AbstractCommand;
import org.eclim.command.CommandLine;
import org.eclim.command.Error;
import org.eclim.command.Options;

import org.eclim.util.XmlUtils;

import org.xml.sax.ContentHandler;

/**
 * Command to validate a xml file.
 *
 * @author Eric Van Dewoestine (ervandew@yahoo.com)
 * @version $Revision$
 */
public class ValidateCommand
  extends AbstractCommand
{
  private static final String NO_GRAMMER = "no grammar found";
  private static final String DOCTYPE_ROOT_NULL = "DOCTYPE root \"null\"";

  /**
   * {@inheritDoc}
   */
  public Object execute (CommandLine _commandLine)
  {
    try{
      String project = _commandLine.getValue(Options.PROJECT_OPTION);
      String file = _commandLine.getValue(Options.FILE_OPTION);
      boolean schema = _commandLine.hasOption(Options.SCHEMA_OPTION);

      List list = validate(project, file, schema, getContentHandler());

      return filter(_commandLine, list.toArray(new Error[list.size()]));
    }catch(Throwable t){
      return t;
    }
  }

  /**
   * Validate the supplied file.
   *
   * @param _project The project name.
   * @param _file The file to validate.
   * @param _schema true to use declared schema, false otherwise.
   * @param _contentHandler The ContentHandler to use while parsing the xml
   * file.
   * @return The list of errors.
   */
  protected List validate (
      String _project, String _file, boolean _schema, ContentHandler _contentHandler)
    throws Exception
  {
    Error[] errors = XmlUtils.validateXml(_project, _file, _schema, _contentHandler);
    ArrayList list = new ArrayList();
    for(int ii = 0; ii < errors.length; ii++){
      // FIXME: hack to ignore errors regarding no defined dtd.
      // When 1.4 no longer needs to be supported, this can be scrapped.
      if (errors[ii].getMessage().indexOf(NO_GRAMMER) == -1 &&
          errors[ii].getMessage().indexOf(DOCTYPE_ROOT_NULL) == -1)
      {
        list.add(errors[ii]);
      }
    }
    return list;
  }

  /**
   * Retrieves the ContentHandler to use while parsing the xml file.
   *
   * @return The ContentHandler.
   */
  protected ContentHandler getContentHandler ()
  {
    return null;
  }
}
