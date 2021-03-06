package com.intellij.remoteServer;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.remoteServer.configuration.ServerConfiguration;
import com.intellij.remoteServer.deployment.Deployer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author nik
 */
public abstract class ServerType<C extends ServerConfiguration> {
  public static final ExtensionPointName<ServerType> EP_NAME = ExtensionPointName.create("com.intellij.remoteServer.type");
  private final String myId;

  protected ServerType(String id) {
    myId = id;
  }

  public final String getId() {
    return myId;
  }

  @NotNull
  public abstract String getPresentableName();

  @NotNull
  public abstract Icon getIcon();

  @NotNull
  public abstract C createDefaultConfiguration();

  @NotNull
  public abstract UnnamedConfigurable createConfigurable(@NotNull C configuration);

  @NotNull
  public abstract Deployer<C> createDeployer(Project project);
}
