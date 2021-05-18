package com.gd.sakila.controller;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gd.sakila.service.BoardService;
import com.gd.sakila.vo.Board;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class BoardController {
	@Autowired
	private BoardService boardService;
	// 리턴타입은 뷰 이름 (문자열)
	
	
	// 글 수정 폼 (UPDATE)
	@GetMapping("/modifyBoard")
	public String modifyBoard(Model model, @RequestParam(value = "boardId", required = true) int boardId) {
		// 디버깅
		log.debug("▶▶▶▶▶▶ modifyBoard param : "+boardId);
		
		Map<String, Object> map = boardService.getBoardOne(boardId);
		
		model.addAttribute("boardId", map.get("boardId"));
		model.addAttribute("boardTitle", map.get("boardTitle"));
		model.addAttribute("boardContent", map.get("boardContent"));
		
		return "modifyBoard";
	}
	
	// 글 수정 액션 (UPDATE)
	@PostMapping("/modifyBoard")
	public String modifyBoard(Board board) {
		// 디버깅
		log.debug("▶▶▶▶▶▶ UPDATE row param : "+ board.getBoardId());
		boardService.modifyBoard(board);
		return "redirect:/admin/getBoardOne?boardId="+board.getBoardId();
	}
	
	// 글 삭제 폼 (DELETE)
	@GetMapping("/removeBoard")
	public String removeBoard(Model model, @RequestParam(value = "boardId", required = true) int boradId) {
		// 디버깅
		log.debug("▶▶▶▶▶▶ param : "+boradId); // <- 문자열을 받아야 하기 때문에 앞에 문자열 추가
		model.addAttribute("boardId", boradId);
		return "removeBoard";
	}
	
	// 글 삭제 액션 (DELETE)
		// C - > M - > redirect
	@PostMapping("/removeBoard")
	public String removeBoard(Board board) {
		int row = boardService.removeBoard(board);
		log.debug("▶▶▶▶▶▶ removeBoard row : "+row);
		if(row == 0) {
			return "redirect:/admin/getBoardOne?boardId="+board.getBoardId();
		} else {
			return "redirect:/admin/getBoardList";
		}
	}
	
	// 글 입력 폼 (INSERT)
	@GetMapping("/addBoard")
	public String addBoard() {
		return "addBoard";
	}
	
	// 글 입력 액션 (INSERT)
	@PostMapping("/addBoard") // post
	public String addBoard(Board board) { // 커맨드객체
		boardService.addBoard(board); // 실행
		return "redirect:/admin/getBoardList"; // 재요청 sendRedirect -> List로 넘어감
	}
	
	// 상세보기 (boardOne)
	@GetMapping("/getBoardOne")
	public String getBoardOne(Model model, 
								@RequestParam(value = "boardId", required = true) int boardId) {
		Map<String, Object> map = boardService.getBoardOne(boardId);
		log.debug("map : "+ map);
		model.addAttribute("boardMap",map.get("boardMap"));
		model.addAttribute("commentList",map.get("commentList"));
		return "getBoardOne";
	}
	
	//리스트 (getBoardList)
	@GetMapping("/getBoardList")
	public String getBoardList(Model model, // model에 담기 
								@RequestParam(value = "currentPage", defaultValue = "1") int currentPage, 
								@RequestParam(value = "rowPerPage", defaultValue = "10") int rowPerPage, 
								@RequestParam(value = "searchWord", required = false) String searchWord) {
			System.out.println(currentPage+" <-- currentPage");
			System.out.println(rowPerPage+" <-- rowPerPage");
			System.out.println(searchWord+" <-- searchWord");
		
		Map<String, Object> map = boardService.getBoardList(currentPage, rowPerPage, searchWord);
		//model.addAttribute("map", map);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("lastPage", map.get("lastPage"));
		model.addAttribute("boardList", map.get("boardList"));
		System.out.println(map+"<--BoardController map");
		return "getBoardList";
	}
}
