/*
 * ArrayList.h
 *
 *  Created on: 2013-1-13
 *      Author: Noisyfox
 */

#ifndef ARRAYLIST_H_
#define ARRAYLIST_H_

//检查是否成功申请内存
#define Asert(par) if(par==NULL) {\
                return NULL;}
#define INIT_SIZE 10    //默认的数组大小
typedef void* Element;    //定义List里的元素类型
typedef void (*PrtFun)(Element);    //打印函数
typedef int (*CmpFun)(Element, Element);    //比较函数
typedef struct { //定义ArrayList

	Element *data; //List的元素
	int size; //List可以容纳个数
	int index; //当前已有元素索引
	CmpFun cmpFun; //比较函数指针
	PrtFun ptrFun; //打印函数指针

} ArrayList;

/*********************************
 *@desc:创建ArrayList
 *@return: NULL->内存申请失败
 arr->指向ArrayList的指针
 **********************************
 */
ArrayList *createArrayList(CmpFun cmp, PrtFun pfun);

/*******************************
 *@desc: 释放ArrayList占有的内存
 ******************************
 */
void desrotyArrList(ArrayList *arr);

/*************************************
 *@desc: 检查链表的剩余空间,不够则申请
 *@param: arr->要检查的链表
 *@notice: 本函数不检查参数是否合法
 *************************************
 */
static Element *checkAndPre(ArrayList *arr);

/****************************
 *@desc: 在末尾添加元素
 *@param: arr->对那个arrlist添加
 *@parm: elm->要添加的元素
 *@return:1->成功
 0->失败
 ******************************
 */
int addElement(ArrayList *arr, Element elm);

/****************************
 *@desc: 把指定的位置设置为elm
 *@param: arr->待插入的列表
 *@param: elm->待插入的元素
 *@param: i->待设置的位置
 *@return:1->成功
 0->失败
 *****************************
 */
int setElement(ArrayList *arr, Element elm, size_t i);

/************************
 *@desc: 打印ArrayList
 *@param:arr->要打印的列表
 *@param:fun->打印函数
 *************************
 **/
void printArrayList(ArrayList *arr);

/************************
 *@desc:列表是否为空
 *param:arr->待检查的列表
 *@return:1->非空 0->空
 ******************* ****
 */
int isEmpty(ArrayList *arr);

/**************************************
 *@desc:获取指定位置的元素
 *@param:arr->待查找的列表
 *@param:index->索引
 *@return:NULL->索引错误否则返回元素指针
 ***************************************
 */
Element *getElement(ArrayList *arr, size_t index);

/*************************
 *@desc：是否包含指定的元素
 *@param: elm->元素
 *@param:  arr->列表
 *@param:  fun->比较函数
 *return 0->包含 -1->不包含
 ****************************
 */
int contains(ArrayList *arr, Element elm);

/**************************
 *@desc:获取某个元素的位置
 *@param:arr>列表
 *@param:elm->元素
 *@param:  fun->比较函数
 *@return:-1->找不到
 **************************
 */
int arrLstIndexof(ArrayList *arr, Element elm);

/************************
 *@desc:移除指定的元素
 *@param:arr->待操作的列表
 *@param:elm->要移除的元素
 *@param:fun->比较函数
 *@return:0->失败1->成功
 **************************
 */
int removeElement(ArrayList *arr, Element elm);

#endif /* ARRAYLIST_H_ */
