package com.gd.sakila.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gd.sakila.mapper.BoardMapper;
import com.gd.sakila.vo.Board;
import com.gd.sakila.vo.Page;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service // 객체생성
@Transactional
public class BoardService {
	@Autowired // 객체를 주입
	private BoardMapper boardMapper;
	
	// 글 삭제
	public int removeBoard(Board board) {
		log.debug(board.toString());
		return boardMapper.deleteBoard(board);
	}
	
	// 글 입력
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
	}
	
	// 상세보기
	public Map<String, Object> getBoardOne(int boardId) {
		return boardMapper.selectBoardOne(boardId);
	}
	
	// 리스트
	public Map<String, Object> getBoardList(int currentPage, int rowPerPage, String searchWord) {
		// lastPage를 구하기 위함
		int boardTotal = boardMapper.selectBoardTotal(searchWord); // searchWord가 필요
		// 1.
		/*
		int lastPage = boardTotal / rowPerPage;
		if(lastPage % rowPerPage != 0) {
			lastPage++;
		}
		*/
		int lastPage = (int)(Math.ceil((double)boardTotal / rowPerPage));
		
		// 2.
		Page page = new Page();
		page.setBeginRow((currentPage-1)*rowPerPage);
		page.setRowPerPage(rowPerPage);
		page.setSearchWord(searchWord);
		List<Board> boardList = boardMapper.selectBoardList(page); // Page가 필요
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lastPage", lastPage);
		map.put("boardList", boardList);
		System.out.println(map+"<-- BoardService map");
		return map;
	}
}
