package org.dbpedia.databus;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Validate setup and resources
 *
 * WebID
 * - dereference and download
 * - get the public key from the webid
 * - get the private key from the config, generate a public key and compare to the public key
 *
 * @phase validate2
 */
@Deprecated
@Mojo(name = "validate2")
public class Validate2 extends AbstractMojo {

    @Parameter
    private String maintainer;

    public void execute()
            throws MojoExecutionException {
    }

}