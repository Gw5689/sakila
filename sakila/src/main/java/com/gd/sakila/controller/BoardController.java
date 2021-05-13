package com.gd.sakila.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gd.sakila.service.BoardService;
import com.gd.sakila.vo.Board;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	// insert
	@GetMapping("/addBoard")
	public String addBoard() {
		return "addBoard";
	}
	
	@PostMapping("/addBoard") // post
	public String addBoard(Board board) { // 커맨드객체
		boardService.addBoard(board); // 실행
		return "redirect:/getBoardList"; // 재요청 sendRedirect -> List로 넘어감
	}
	
	// 상세보기
	@GetMapping("/getBoardOne")
	public String getBoardOne(Model model, 
								@RequestParam(value = "boardId", required = true) int boardId) {
		Map<String, Object> map = boardService.getBoardOne(boardId);
		System.out.println(map);
		model.addAttribute("map",map);
		return "getBoardOne";
	}
	
	//리스트 
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
