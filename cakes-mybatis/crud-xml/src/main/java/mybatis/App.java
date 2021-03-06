package mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import mybatis.domain.QueryTransVO;
import mybatis.domain.Trans;
import mybatis.mapper.TransMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author haoc
 */
public class App {

  private InputStream inputStream;

  private SqlSession sqlSession;

  private TransMapper transMapper;

  /**
   * 插入测试
   */
  @Test
  public void testInsert() {
    Trans trans = new Trans();
    trans.setTrans_id(System.currentTimeMillis() + "");
    trans.setAmount(1024L);
    trans.setBody("其他交易");
    trans.setSubject("支付");
    trans.setCreate_time(new Date());

    int effectRows = transMapper.insertTrans(trans);

    sqlSession.commit();

    Assert.assertEquals(1, effectRows);
  }

  /**
   * 插入并返回id测试
   */
  @Test
  public void testInsertAndIdReturn() {
    Trans trans = new Trans();
    trans.setTrans_id(System.currentTimeMillis() + "");
    trans.setAmount(1024L);
    trans.setBody("其他交易-id");
    trans.setSubject("支付-id");
    trans.setCreate_time(new Date());

    System.out.println("before insert:" + trans);

    int effectRows = transMapper.insertTransAndIdReturn(trans);

    System.out.println("after insert:" + trans);

    sqlSession.commit();

    System.out.println("insert trans ,and effect rows =" + effectRows);
  }

  /**
   * 修改测试
   */
  @Test
  public void testUpdate() {
    Trans trans = new Trans();
    trans.setTrans_id("1558017429079");
    trans.setAmount(2048L);
    trans.setBody("其他交易");
    trans.setSubject("支付");

    int effectRows = transMapper.updateTrans(trans);

    sqlSession.commit();

    Assert.assertEquals(1, effectRows);
  }

  /**
   * 删除测试
   */
  @Test
  public void testDelete() {
    int effectRows = transMapper.deleteTransById(7L);

    sqlSession.commit();

    Assert.assertEquals(1, effectRows);
  }

  /**
   * 查询一条记录
   */
  @Test
  public void testSelectOne() {
    Trans trans = transMapper.selectOne("1558017429079");

    System.out.println(trans);
  }

  /**
   * 查询多条
   */
  @Test
  public void testSelectByBody() {
    // #{body}
    List<Trans> trans = transMapper.selectByBody("%支付%");
    trans.forEach(System.out::println);
  }

  /**
   * 查询多条
   */
  @Test
  public void testSelectByFixedBody() {
    // #{body}
    List<Trans> trans = transMapper.selectByFixedBody("支付");
    trans.forEach(System.out::println);
  }

  /**
   * 查询条包装
   */
  @Test
  public void testSelectByQueryVO() {
    Trans trans = new Trans();
    trans.setBody("其他交易");
    trans.setSubject("支付");

    QueryTransVO queryTransVO = new QueryTransVO();
    queryTransVO.setTrans(trans);

    List<Trans> transes = transMapper.selectByQueryVO(queryTransVO);
    transes.forEach(System.out::println);
  }

  /**
   * 查询全部
   */
  @Test
  public void testGetTotal() {
    int total = transMapper.getTotal();
    System.out.println("total: " + total);
  }

  @Before
  public void init() throws IOException {
    inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");

    // 写法1
    // SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    // sqlSession = sessionFactory.openSession();

    // 写法2
    sqlSession = new SqlSessionFactoryBuilder().build(inputStream).openSession();

    transMapper = sqlSession.getMapper(TransMapper.class);
  }

  @After
  public void destory() throws IOException {
    sqlSession.close();
    inputStream.close();
  }

}
