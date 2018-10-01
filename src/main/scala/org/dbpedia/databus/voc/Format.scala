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
package org.dbpedia.databus.voc

import java.io.File


class Format(val mimeType: String = "UNKNOWN", val lineBased: Boolean = false, val rio: org.eclipse.rdf4j.rio.RDFFormat, val jena: org.apache.jena.riot.RDFFormat) {
  override def toString = s"Format($mimeType)"
}


object Format {

  lazy val knownFormats = Map(
    ".nt" -> ApplicationNTriples,
    ".ttl" -> TextTurtle,
    ".tql" -> ApplicationNQuad,
    ".nq" -> ApplicationNQuad,
    ".rdf" -> ApplicationRDFXML,
    ".csv" -> TextCSV,
    ".tsv" -> TextTabSeparatedValues
  )


  def detectMimetypeByFileExtension(datafile: File): (String, Format) = {

    val extensionMatch = knownFormats find { case (ext, _) => datafile.getName.contains(ext) }

    extensionMatch match {

      case Some(matchedExtAndFormat) => matchedExtAndFormat

      case None => ("", UNKNOWN)
    }
  }
}

// line base
object ApplicationNTriples extends Format("application/n-triples", true, org.eclipse.rdf4j.rio.RDFFormat.NTRIPLES, org.apache.jena.riot.RDFFormat.NTRIPLES) {}

object ApplicationNQuad extends Format("application/n-quads", true, org.eclipse.rdf4j.rio.RDFFormat.NQUADS, org.apache.jena.riot.RDFFormat.NQUADS) {}


// other
object TextTurtle extends Format("text/turtle", false, org.eclipse.rdf4j.rio.RDFFormat.TURTLE, org.apache.jena.riot.RDFFormat.TURTLE) {}

object ApplicationRDFXML extends Format("application/rdf+xml", false, org.eclipse.rdf4j.rio.RDFFormat.RDFXML, org.apache.jena.riot.RDFFormat.RDFXML) {}

object TextCSV extends Format(mimeType = "text/csv", lineBased = true, null, null)

object TextTabSeparatedValues extends Format("text/tab-separated-values", lineBased = true, null, null)

object UNKNOWN extends Format("UNKNOWN", false, null, null) {}


