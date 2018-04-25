/**
 * \* Created with IntelliJ IDEA.
 * \* User: llf
 * \* Date: 2018/4/25
 * \* Time: 22:31
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
package com.example.demo.Dao;

import com.example.demo.Entity.Memorial;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface MemorialMapper {

    @Insert("insert into d_star(name, avatar) values(#{avatar}, #{avatar})")
    int add(@Param("name") String name, @Param("avatar") String avatar);

    @Update("update d_star set name = #{name}, avatar = #{avatar} where id = #{id}")
    int update(@Param("name") String name, @Param("money") String money, @Param("id") int id);

    @Delete("delete from d_star where id = #{id}")
    int delete(int id);

    @Select("select id, name as name, avatar as avatar from d_star where id = #{id}")
    Memorial findMemorial(@Param("id") int id);

    @Select("select id, name as name, avatar as avatar from d_star")
    List<Memorial> findMemorialList();
}