package com.gd.sakila.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gd.sakila.mapper.CommentMapper;
import com.gd.sakila.vo.Comment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CommentService {
	@Autowired private CommentMapper commentMapper;
	
	// 댓글 개별 삭제
	public int removeComment(int commentId) {
		int removeComment = commentMapper.deleteCommentByCommentId(commentId);
		// 디버깅
		log.debug("▶▶▶▶▶ (S)removeComment param : "+removeComment);
		// 리턴
		return removeComment;
	}
	
	// 댓글 입력
	public int addComment(Comment comment) {
		int addComment = commentMapper.insertComment(comment);
		// 디버깅
		log.debug("▶▶▶▶▶ (S)addComment param : "+addComment);
		// 리턴
		return addComment;
	}
	
}
