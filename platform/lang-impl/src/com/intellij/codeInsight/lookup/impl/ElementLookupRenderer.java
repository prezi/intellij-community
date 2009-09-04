package com.intellij.codeInsight.lookup.impl;

import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.openapi.extensions.ExtensionPointName;

/**
 * @author yole
 * @deprecated use {@link com.intellij.codeInsight.lookup.LookupElement#renderElement(com.intellij.codeInsight.lookup.LookupElementPresentation)}
 */
@Deprecated
public interface ElementLookupRenderer<T> {
  ExtensionPointName<ElementLookupRenderer> EP_NAME = ExtensionPointName.create("com.intellij.elementLookupRenderer");

  boolean handlesItem(Object element);
  void renderElement(final LookupItem item, T element, LookupElementPresentation presentation);

}
