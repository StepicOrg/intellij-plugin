/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
 */
package org.stepic.plugin.toolWindow;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBCardLayout;
import com.intellij.ui.OnePixelSplitter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public abstract class StudyToolWindow extends SimpleToolWindowPanel implements DataProvider, Disposable {
  private static final Logger LOG = Logger.getInstance(StudyToolWindow.class);
  private static final String STEP_INFO_ID = "stepInfo";
  private final JBCardLayout myCardLayout;
  private final JPanel myContentPanel;
  private final OnePixelSplitter mySplitPane;

  public StudyToolWindow() {
    super(true, true);
    myCardLayout = new JBCardLayout();
    myContentPanel = new JPanel(myCardLayout);
    mySplitPane = new OnePixelSplitter(myVertical=true);
  }

  public void init(Project project) {
    String stepText = StudyUtils.getStepText(project);
    if (stepText == null) return;

//    JPanel toolbarPanel = createToolbarPanel(project);
//    setToolbar(toolbarPanel);

    myContentPanel.add(STEP_INFO_ID, createStepInfoPanel(stepText, project));
    mySplitPane.setFirstComponent(myContentPanel);
    addAdditionalPanels(project);
    myCardLayout.show(myContentPanel, STEP_INFO_ID);

    setContent(mySplitPane);

    StudyToolWindowConfigurator configurator = StudyUtils.getConfigurator(project);
    assert configurator != null;
    final FileEditorManagerListener listener = configurator.getFileEditorManagerListener(project, this);
    project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, listener);
  }

  private void addAdditionalPanels(Project project) {
//    StudyToolWindowConfigurator configurator = StudyUtils.getConfigurator(project);
//    assert configurator != null;
//    Map<String, JPanel> panels = configurator.getAdditionalPanels(project);
//    for (Map.Entry<String, JPanel> entry: panels.entrySet()) {
//      myContentPanel.add(entry.getKey(), entry.getValue());
//    }
  }

  public void dispose() {
  }
  
  //used in checkiO plugin.
  @SuppressWarnings("unused")
  public void showPanelById(@NotNull final String panelId) {
    myCardLayout.swipe(myContentPanel, panelId, JBCardLayout.SwipeDirection.AUTO);
  }

  //used in checkiO plugin.
  @SuppressWarnings("unused")
  public void setBottomComponent(JComponent component) {
    mySplitPane.setSecondComponent(component);
  }

  //used in checkiO plugin.
  @SuppressWarnings("unused")
  public JComponent getBottomComponent() {
    return mySplitPane.getSecondComponent();
  }

  //used in checkiO plugin.
  @SuppressWarnings("unused")
  public void setTopComponentPrefferedSize(@NotNull final Dimension dimension) {
    myContentPanel.setPreferredSize(dimension);
  }

  //used in checkiO plugin.
  @SuppressWarnings("unused")
  public JPanel getContentPanel() {
    return myContentPanel;
  }


  public abstract JComponent createStepInfoPanel(String stepText, Project project);

//  private static JPanel createToolbarPanel(@NotNull final Project project) {
//    final DefaultActionGroup group = getActionGroup(project);

//    final ActionToolbar actionToolBar = ActionManager.getInstance().createActionToolbar("Study", group, true);
//    return JBUI.Panels.simplePanel(actionToolBar.getComponent());
//  }

//  private static DefaultActionGroup getActionGroup(@NotNull final Project project) {
//    Course course = StudyTaskManager.getInstance(project).getCourse();
//    if (course == null) {
//      LOG.warn("Course is null");
//      return new DefaultActionGroup();
//    }
//    StudyToolWindowConfigurator configurator = StudyUtils.getConfigurator(project);
//    assert configurator != null;
//
//    return configurator.getActionGroup(project);
//  }

  public abstract void setStepText(String text) ;
}
