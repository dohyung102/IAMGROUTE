package com.ssafy.groute.mapper.board;

import com.ssafy.groute.dto.board.Comment;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface CommentMapper {
    void insertComment(Comment comment) throws Exception;
    Comment selectComment(int id) throws Exception;
    List<Comment> selectAllComment() throws Exception;
    int deleteComment(Comment comment) throws Exception;
    void updateComment(Comment comment) throws Exception;
    List<Comment> selectAllByBoardDetailId(int id) throws Exception;
    void deleteAllCommentByBoardDetailId(int boardDetailId) throws Exception;
    void deleteAllCommentByUId(String userId) throws Exception;
    void deleteReComment(int id);
}
