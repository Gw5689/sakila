package com.gd.sakila.service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gd.sakila.mapper.BoardMapper;
import com.gd.sakila.mapper.CommentMapper;
import com.gd.sakila.vo.Board;
import com.gd.sakila.vo.Comment;
import com.gd.sakila.vo.Page;

import lombok.extern.slf4j.Slf4j;

@Slf4j // log 객체 생성
@Service // 객체생성 (spring이 스캔)
@Transactional // 커밋, 롤백 (예외가 발생하면 롤백)
public class BoardService {
	@Autowired private BoardMapper boardMapper; // 스캔한 객체가 있으면 주입
	@Autowired private CommentMapper commentMapper;
	
	
	
	// 글 수정
	public int modifyBoard(Board board) {
		log.debug("▶▶▶▶▶ modifyBoard param : "+board.toString());
		return boardMapper.updateBoard(board);
	}
	
	// 글 삭제
	public int removeBoard(Board board) {
		log.debug("▶▶▶▶▶ removeBoard param : "+board.toString());
		
		// 2) 게시글 삭제
		int boardRow = boardMapper.deleteBoard(board);
		if(boardRow == 0) { // 게시물이 삭제가 안되면 롤백 
			return 0;
		}
		
		// 1) 댓글 삭제
		int commentRow = commentMapper.deleteCommentByBoardId(board.getBoardId());
		
		// 디버깅
		log.debug("▶▶▶▶▶ removeBoard commentRow param : "+commentRow);
		log.debug("▶▶▶▶▶ removeBoard boardRow param : "+boardRow);
		
		return boardRow;

	}
	
	// 글 입력
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
	}
	
	// 1) 상세보기 + 2)댓글목록 수정 폼
	public Map<String, Object> getBoardOne(int boardId) {
		// 1) 상세보기
		Map<String, Object> boardMap = boardMapper.selectBoardOne(boardId);
			// 디버깅
		log.debug("boardMap : "+boardMap);
		// 2) 댓글 목록
		List<Comment> commentList = commentMapper.selectCommentListByBoard(boardId);
			// 디버깅
		log.debug("list size() : "+commentList.size());
		
		Map<String, Object> map = new HashMap<>(); // Map 안에 Map(boardMap, commentList)을 넣음
		map.put("boardMap", boardMap);
		map.put("commentList", commentList);
		return map;
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
