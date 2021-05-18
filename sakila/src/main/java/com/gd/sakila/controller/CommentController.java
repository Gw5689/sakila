package com.gd.sakila.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gd.sakila.service.CommentService;
import com.gd.sakila.vo.Comment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class CommentController {
	@Autowired CommentService commentService;
	
	// 댓글 삭제 (DELETE)
	@GetMapping("/removeComment")
	public String removeComment(@RequestParam(value = "commentId", required = true) int commentId, // 필수입력
			 					@RequestParam(value = "boardId", required = true) int boardId) {
		// 디버깅
		log.debug("▶▶▶▶▶▶ (C)removeComment commentId param : "+commentId);
		log.debug("▶▶▶▶▶▶ (C)removeComment boardId param : "+boardId);
		int removeCommentRow = commentService.removeComment(commentId); // 서비스에서 removeComment 호출
		
		// 디버깅
		log.debug("▶▶▶▶▶▶ (C)removeCommentRow param : "+removeCommentRow);
		
		return "redirect:/admin/getBoardOne?boardId="+boardId; // 삭제하고 다시 One 페이지로 redirect
		
	}
	

	// 댓글 입력
	@PostMapping("/addComment")
	public String addComment(Comment comment, @RequestParam(value="boardId", required = true) int boardId) {

		// 디버깅
		log.debug("▶▶▶▶▶▶ (C)addComment param : "+comment.toString());
		int addCommentRow = commentService.addComment(comment); // 서비스에서 addComment 호출
		
		// 디버깅
		log.debug("▶▶▶▶▶▶ (C)addCommentRow param : "+addCommentRow);
		
		return "redirect:/admin/getBoardOne?boardId="+boardId; // 입력하고 다시 One 페이지로 redirect
	}
	
}
