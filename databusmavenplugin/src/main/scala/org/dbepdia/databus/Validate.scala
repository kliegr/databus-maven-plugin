/*-
 * #%L
 * databus-maven-plugin
 * %%
 * Copyright (C) 2018 Sebastian Hellmann (on behalf of the DBpedia Association)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.dbpedia.databus


import java.io.File
import java.net.URL
import java.security.interfaces.RSAPrivateCrtKey
import java.util

import org.apache.jena.rdf.model.{ModelFactory, NodeIterator, RDFNode}
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.{LifecyclePhase, Mojo, Parameter}
import org.dbepdia.databus.lib.{Sign}


/**
  * Validate setup and resources
  *
  * WebID
  * - dereference and download
  * - get the public key from the webid
  * - get the private key from the config, generate a public key and compare to the public key
  *
  */
@Mojo(name = "validate", defaultPhase = LifecyclePhase.VALIDATE)
class Validate extends AbstractMojo {


  @Parameter(defaultValue = "${project.artifactId}", readonly = true)
  private val artifactId: String = ""

  @Parameter(defaultValue = "${project.packaging}", readonly = true)
  private val packaging: String = ""


  @Parameter var maintainer: URL = _
  @Parameter var privateKeyFile: File = _

  //@Parameter var contentVariants:util.ArrayList[ContentVariant] = null
  @Parameter var contentVariants: util.ArrayList[String] = _
  @Parameter var formatVariants: util.ArrayList[String] = _

  @throws[MojoExecutionException]
  override def execute(): Unit = {
    if (packaging.equals("pom")) {

      validateWebId()

    } else {

      validateFileNames()

    }
  }

  /**
    * for now just prints all keys
    */
  def validateWebId(): Unit = {

    /**
      * Read the webid
      */
    val model = ModelFactory.createDefaultModel
    model.read(maintainer.toString)
    getLog.info("Read " + model.size() + " triples from " + maintainer)
    val ni: NodeIterator = model.listObjectsOfProperty(model.getResource(maintainer.toString), model.getProperty("http://www.w3.org/ns/auth/cert#key"))

    //TODO validate against the private key
    getLog.info("Private Key File: " + privateKeyFile)
    while (ni.hasNext) {
      var node: RDFNode = ni.next()
      var modulus = model.listObjectsOfProperty(node.asResource(), model.getProperty("http://www.w3.org/ns/auth/cert#modulus")).next()
      var exponent = model.listObjectsOfProperty(node.asResource(), model.getProperty("http://www.w3.org/ns/auth/cert#exponent")).next()
      //TODO fix paths
      val fileHack: File = new File(privateKeyFile.getAbsolutePath.replace("${project.parent.basedir}/", ""))

      val privateKey = Sign.readPrivateKeyFile(fileHack)
      //TODO hexadecimalformat
      val privk: RSAPrivateCrtKey = privateKey.asInstanceOf[RSAPrivateCrtKey]

      getLog.info("BlankNode: " + node + "")
      getLog.info("Exponent (from webid): " + exponent.asLiteral().getLexicalForm)
      getLog.info("Exponent (from privk): " + privk.getPublicExponent)
      getLog.info("Modulus (from webid): " + modulus.asLiteral().getLexicalForm)
      //getLog.info("Modulus (from webid): " + Long.parseInt(modulus.asLiteral().getLexicalForm, 16))
      getLog.info("Modulus (from privk): " + privk.getModulus)
      //getLog.info("Modulus (from privk): " +  java.lang.Long.valueOf(privk.getModulus.toString,16))


    }

  }

  def validateFileNames(): Unit = {

  }


}


