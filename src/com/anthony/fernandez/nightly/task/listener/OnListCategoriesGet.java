package com.anthony.fernandez.nightly.task.listener;

import java.util.ArrayList;

import com.anthony.fernandez.nightly.model.Category;

public interface OnListCategoriesGet {

	public void OnGetListCategories(ArrayList<Category> listCategories);
	public void OnGetListCategoriesFailed(String reason);
}
