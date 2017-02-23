package com.cxdb.datasource.model;

/**
 * 分页javabean
 * 
 */
public class PageBean {
	private int currentPage;
	private int pageSize;
	private int startNum;
	private int totalNum;
	private int totalPage;

	/**
	 * @param currentPage
	 *            ：当前的页码，从1开始
	 * @param pageSize
	 *            ：每一页的数量
	 */
	public PageBean(int currentPage, int pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.startNum = (currentPage - 1) * pageSize;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
		if (totalNum <= 0) {
			this.totalNum = 0;
			this.totalPage = 1;
		} else if (totalNum % pageSize == 0) {
			this.totalPage = totalNum / pageSize;
		} else {
			this.totalPage = totalNum / pageSize + 1;
		}

	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getStartNum() {
		return startNum;
	}

	public int getTotalPage() {
		return totalPage;
	}

	@Override
	public String toString() {
		return "PageBean [currentPage=" + currentPage + ", pageSize="
				+ pageSize + ", startNum=" + startNum + ", totalNum="
				+ totalNum + ", totalPage=" + totalPage + "]";
	}

}
