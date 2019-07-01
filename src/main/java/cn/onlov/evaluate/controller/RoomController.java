package cn.onlov.evaluate.controller;

import cn.onlov.evaluate.core.dao.entities.CycleRoom;
import cn.onlov.evaluate.core.dao.interfaces.ICycleRoomService;
import cn.onlov.evaluate.pojo.bo.CycleRoomBo;
import cn.onlov.evaluate.service.CycleRoomService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangqj on 2017/4/26.
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Resource
    private CycleRoomService cycleRoomService;
    @Resource
    private ICycleRoomService iCycleRoomService;
    @RequestMapping
    public  Map<String,Object> getAll(CycleRoom room, String draw,
                                      @RequestParam(required = false, defaultValue = "1") int start,
                                      @RequestParam(required = false, defaultValue = "10") int length){

        Map<String,Object> map = new HashMap<>();
        CycleRoomBo cycleRoomBo  = new CycleRoomBo();
        BeanUtils.copyProperties(room,cycleRoomBo);
        cycleRoomBo.setCurr(start);
        cycleRoomBo.setPageSize(length);
        IPage<CycleRoom> pageInfo = cycleRoomService.selectByPage(cycleRoomBo);
        map.put("draw",draw);
        map.put("recordsTotal",pageInfo.getTotal());
        map.put("recordsFiltered",pageInfo.getTotal());
        map.put("data", pageInfo.getRecords());
        return map;
    }

    @RequestMapping("getAll")
    public  Map<String,Object> getAll(){
        Map<String,Object> map = new HashMap<>();
        boolean success = false;
        String msg = "获取数据失败！";
        Object data = null;

        List<CycleRoom> list = null;
        try {
            list = cycleRoomService.selectAll();
            success = true;
            data = list;
            msg = "获取数据成功";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        map.put("success", success);
        map.put("data", data);
        map.put("msg", msg);
        return map;
    }

    @RequestMapping(value = "/add")
    public String add(CycleRoom room) {
        try {
            iCycleRoomService.save(room);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/delete")
    public String delete(Integer id){
        try{
            cycleRoomService.deleteByKey(id);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/update")
    public String update(CycleRoom room) {
        try {
            iCycleRoomService.saveOrUpdate(room);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }



}
