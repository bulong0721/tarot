package com.myee.tarot.web.apiold.util;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 * @CreateDate 2010-8-2
 * @version 1.0.01
 */
public abstract class MyArrayUtils extends ArrayUtils {

	/**
	 * 合并int数组到一个字符串
	 * 
	 * @param arrayObj
	 *            要处理的int数组
	 * @param seperator
	 *            分隔符，比如,
	 * @return 字符串，比如simon,peter,helloworld
	 */
	public static <T> String merge(int[] arrayObj, String seperator) {
		return merge(toObject(arrayObj), seperator, "");
	}

	/**
	 * 合并字符串数组到一个字符串
	 * @param arrayObj	要处理的字符串数组
	 * @param seperator	分隔符，比如,
	 * @return	字符串，比如simon,peter,helloworld
	 */
	public static <T> String merge(String[] arrayObj, String seperator){
		return merge(arrayObj, seperator, "");
	}
	
	/**
	 * 合并对象列表到一个字符串
	 * @param listObj	要处理的对象列表
	 * @return	字符串，比如12,16,17
	 */
	public static <T> String merge(List<T> listObj){
		if(listObj == null){
			return null;
		}
		return merge(listObj.toArray(), ",", "");
	}
	
	/**
	 * 合并对象列表到一个字符串
	 * @param listObj	要处理的对象列表
	 * @param seperator	分隔符，比如,
	 * @return	字符串，比如12,16,17
	 */
	public static <T> String merge(List<T> listObj, String seperator){
		if(listObj == null){
			return null;
		}
		return merge(listObj.toArray(), seperator, "");
	}

	/**
	 * 合并对象数组到一个字符串
	 * @param arrayObj	要处理的对象数组，比如{12,16,17}
	 * @param seperator	分隔符，比如,
	 * @return	字符串，比如12,16,17
	 */
	public static <T> String merge(T[] arrayObj, String seperator){
		return merge(arrayObj, seperator, "");
	}

	/**
	 * 合并对象数组到一个字符串
	 * @param arrayObj	要处理的对象数组，比如{12,16,17}
	 * @param seperator	分隔符，比如,
	 * @param quoteStr	对象引用符，比如"
	 * @return	字符串，比如"12","16","17"
	 */
	public static <T> String merge(T[] arrayObj, String seperator, String quoteStr){
		if(arrayObj == null)
			return null;
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<Array.getLength(arrayObj); i++){
			if(i>0){
				sb.append(seperator);
			}
			sb.append(quoteStr);
			sb.append(Array.get(arrayObj, i));
			sb.append(quoteStr);
		}
		return sb.toString();
	}	

	public static int[] split(String arrayStr, String seperator){
		if(arrayStr == null || arrayStr.equals(""))
			return null;
		
		int[] returnArray = null;
		String[] elements = arrayStr.split(seperator);
		if(elements != null && elements.length > 0){
			returnArray = new int[elements.length];
			for(int i=0; i< elements.length; i++){
				returnArray[i] = (int)(Integer.valueOf(elements[i]));
			}
		}
		return returnArray;
	}	

	public static String[] split2(String arrayStr, String seperator){
		if(arrayStr == null || arrayStr.equals(""))
			return null;
		
		String[] returnArray = null;
		String[] elements = arrayStr.split(seperator);
		if(elements != null && elements.length > 0){
			returnArray = new String[elements.length];
			for(int i=0; i< elements.length; i++){
				returnArray[i] = elements[i];
			}
		}
		return returnArray;
	}	

	/***
	 * 把新数组添加到旧数组末尾
	 * @param oldArray
	 * @param newArray
	 * @return oldArray+newArray
	 */
	public static byte[] append(byte[] oldArray, byte[] newArray){
		return addAll(oldArray, newArray);
		/**
		if(oldArray == null){
			return newArray;
		}
		if(newArray == null){
			return oldArray;
		}
		byte[] totalArray = new byte[oldArray.length + newArray.length];
		System.arraycopy(oldArray, 0, totalArray, 0, oldArray.length);
		System.arraycopy(newArray, 0, totalArray, oldArray.length, newArray.length);
		return totalArray;
		*/
	}	

	/***
	 * 把第一个数组和第二个数组一一对应的相加
	 * @param firstArray 例如：“1,2,3”
	 * @param secondArray 例如：“1,1,1,1”
	 * @param seperator
	 * @return firstArray[i]+secondArray[i]
	 */
	public static String add(String firstArray, String secondArray, String seperator){
		int[] array1 = split(firstArray, seperator);
		int[] array2 = split(secondArray, seperator);
		return MyArrayUtils.merge(MyArrayUtils.toObject(MyArrayUtils.add(array1, array2)), seperator);
	}

	/***
	 * 把第一个数组和第二个数组一一对应的相加
	 * @param firstArray
	 * @param secondArray
	 * @return firstArray[i]+secondArray[i]
	 */
	public static int[] add(int[] firstArray, int[] secondArray){
		if(firstArray == null){
			return secondArray;
		}
		if(secondArray == null){
			return firstArray;
		}
		
		int biggerLenth = firstArray.length > secondArray.length ? firstArray.length : secondArray.length;
		int[] biggerArray = new int[biggerLenth];
		int[] smallerArray = new int[biggerLenth];
		if(firstArray.length > secondArray.length){
			biggerArray = firstArray;
			smallerArray = secondArray;
		}else{
			biggerArray = secondArray;
			smallerArray = firstArray;
		}

		int[] totalArray = new int[biggerLenth];
		System.arraycopy(smallerArray, 0, totalArray, 0, smallerArray.length);
		
		for (int i = 0; i < totalArray.length; i++) {
			totalArray[i] += biggerArray[i];
		}
		return totalArray;
	}

	/**
	 * 从小到大排序 基本思想：在要排序的一组数中，假设前面(n-1)[n>=2] 个数已经是排好顺序的，
	 * 现在要把第n个数插到前面的有序数中，使得这n个数也是排好顺序的。 如此反复循环，直到全部排好顺序。
	 */
	public static int[] insertionSort(List<Integer> origiList) {
		if (origiList == null || origiList.size() == 0) {
			return null;
		}
		List<Integer> notNullArray = new ArrayList();
		for (int n = 0; n < origiList.size(); n++) {
			Integer aInt = origiList.get(n);
			if(aInt != null){
				notNullArray.add(aInt);
			}
		}
		int[] origiArray = new int[notNullArray.size()];
		for (int n = 0; n < notNullArray.size(); n++) {
//			origiArray[n] = TypeConverter.toInteger(notNullArray.get(n));
		}
		return insertionSort(origiArray);
	}

	/**
	 * 从小到大排序 基本思想：在要排序的一组数中，假设前面(n-1)[n>=2] 个数已经是排好顺序的，
	 * 现在要把第n个数插到前面的有序数中，使得这n个数也是排好顺序的。 如此反复循环，直到全部排好顺序。
	 */
	public static int[] insertionSort(int[] origiArray) {
		int current = 0;
		for (int n = 1; n < origiArray.length; n++) {
			int prevIndex = n - 1;
			
			current = origiArray[n];
			for (; prevIndex >= 0 && current < origiArray[prevIndex]; prevIndex--) {
				// 将大于current的值整体后移一个单位
				origiArray[prevIndex + 1] = origiArray[prevIndex];
			}
			// 讲当前节点赋值给恰好小于它的那个元素，注意prevIndex多减了一次（=prevIndex-1）
			origiArray[prevIndex + 1] = current;
		}
		return origiArray;
	}
	
	public static void main(String[] args){
		int[] smallerArray = new int[]{1,2,3};
		int[] biggerArray = new int[]{1,1,1,1};
		System.out.println(add("1,2,3", "1,1,1,1", ","));
	}
}
