/*
 * ArrayList.c
 *
 *  Created on: 2013-1-13
 *      Author: Noisyfox
 */

#include<stdlib.h>

#include<ArrayList.h>

int defaultCmp(Element e1, Element e2) {
	if (e1 == e2)
		return 0;
	return -1;
}

ArrayList *createArrayList(CmpFun cmp, PrtFun pfun) {
	//申请数组的内存
	ArrayList *arr = (ArrayList *) malloc(sizeof(ArrayList));
	Asert(arr);
	Element *data = (Element *) malloc(INIT_SIZE * sizeof(Element));
	Asert(data);
	arr->data = data;
	arr->index = 0;
	arr->size = INIT_SIZE;
	arr->cmpFun = cmp == NULL ? defaultCmp : cmp;
	arr->ptrFun = pfun;
	return arr;
}

void destroyArrayList(ArrayList *arr) {
	if (arr != NULL) {
		free(arr->data); //释放数组占用的内存
		free(arr);       //释放结构体占用的内存
	}
}

static Element *checkAndPre(ArrayList *arr) {
	size_t index = arr->index;
	size_t size = arr->size;
	//没有足够的空间
	if (index >= size) {
		//申请size+INIT_SIZE
		size_t newSize = INIT_SIZE + size;
		Element *newData = (Element*) malloc(newSize * sizeof(Element));
		Asert(newData);
		Element *src = arr->data;
		//复制原来的数据
		size_t i;
		for (i = 0; i < index; i++)
			newData[i] = src[i];
		//释放原来的数据占据的空间
		free(src);
		arr->data = newData;
		arr->size = newSize;
		return newData;
	}
	return arr->data;
}

int addElement(ArrayList *arr, Element elm) {
	if (arr != NULL) {
		//检查剩余空间
		if (checkAndPre(arr) != NULL) {
			Element *els = arr->data;
			els[arr->index++] = elm;
			return 1;
		}
	}
	return 0;
}

int setElement(ArrayList *arr, Element elm, size_t i) {
	if (arr != NULL || i >= arr->index) {
		Element *els = arr->data;
		els[i] = elm;
		return 1;
	}
	return 0;
}

void printArrayList(ArrayList *arr) {
	if (arr != NULL) {
		Element *data = arr->data;
		if (data != NULL) {
			size_t i;
			for (i = 0; i < arr->index; i++)
				arr->ptrFun(*data++);
		}
	}
}

int isEmpty(ArrayList *arr) {
	if (arr != NULL)
		return arr->index > 0;
	return 0;
}

Element *getElement(ArrayList *arr, size_t index) {
	if (arr == NULL || index >= arr->index)
		return NULL;
	return &arr->data[index];
}

int contains(ArrayList *arr, Element elm) {
	return arrLstIndexof(arr, elm) > 0 ? 0 : -1;
}

int arrLstIndexof(ArrayList *arr, Element elm) {
	if (arr != NULL) {
		Element *data = arr->data;
		size_t i;
		for (i = 0; i < arr->index; i++)
			if (arr->cmpFun(data[i], elm) == 0)
				return i;
	}
	return -1;
}

int removeElement(ArrayList *arr, Element elm) {
	if (arr != NULL && arr->data != NULL) {
		int i = arrLstIndexof(arr, elm);
		if (i > 0) {
			Element *data = arr->data;
			arr->index--;
			for (; i < arr->index; i++) {
				data[i] = data[i + 1];
			}
			return 1;
		}
	}
	return 0;
}

int removeElementAt(ArrayList *arr, int index) {
	if (index > arr->index)
		return 0;

	arr->index--;
	for (int i = index; i < arr->index; i++) {
		arr->data[i] = arr->data[i + 1];
	}

	return 1;
}

