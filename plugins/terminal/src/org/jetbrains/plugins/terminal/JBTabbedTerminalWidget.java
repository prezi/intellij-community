package org.jetbrains.plugins.terminal;

import com.intellij.ui.components.JBTabbedPane;
import com.jediterm.terminal.ui.JediTermWidget;
import com.jediterm.terminal.ui.SystemSettingsProvider;
import com.jediterm.terminal.ui.TabbedTerminalWidget;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author traff
 */
public class JBTabbedTerminalWidget extends TabbedTerminalWidget {
  public JBTabbedTerminalWidget(@NotNull SystemSettingsProvider settingsProvider) {
    super(settingsProvider);
  }

  @Override
  protected JediTermWidget createInnerTerminalWidget() {
    return new JBTerminalWidget(getSystemSettingsProvider());
  }

  @Override
  protected JTabbedPane createTabbedPane() {
    return new JBTabbedPane();
  }
}
