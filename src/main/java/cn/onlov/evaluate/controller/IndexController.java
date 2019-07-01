package cn.onlov.evaluate.controller;

import cn.onlov.evaluate.core.dao.entities.*;
import cn.onlov.evaluate.service.*;
import cn.onlov.evaluate.util.MyStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yangqj on 2017/4/21.
 */
@Controller
public class IndexController {


	@Resource
	private UserService cycleUserService;
	@Resource
	private CycleRoleService cycleRoleService;
	@Resource
	private CycleBaseService cycleBaseService;
    @Resource
    private CycleRoomService cycleRoomService;


    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String login(HttpServletRequest request, User user, Model model){
        if ( !MyStringUtils.isNotEmpty(user.getLoginName()) || !MyStringUtils.isNotEmpty(user.getUserPwd())) {
            request.setAttribute("msg", "用户名或密码不能为空！");
            return "/login";
        }
        User u = cycleUserService.selectByLoginName(user.getLoginName());
        if(u==null || u.getIdentityId()!=user.getIdentityId()){
            request.setAttribute("msg", "用户不存在！");
            return "/login";
        }
        UsernamePasswordToken token = null;
        try {
        	Subject subject = SecurityUtils.getSubject();
        	if(user.getUserPwd()!=null){
        		token=new UsernamePasswordToken(user.getLoginName(),user.getUserPwd());
        		subject.login(token);
        		if(user.getIdentityId()==2)//如果是学生，跳转到学生页面
        			return "redirect:/myExams/examsPage";
        		return "redirect:usersPage";
        	}
        }catch (LockedAccountException lae) {
            request.setAttribute("msg", "用户已经被锁定不能登录，请与管理员联系！");
        } catch (AuthenticationException e) {
            request.setAttribute("msg", "用户或密码不正确！");
        }
        
        if(token!=null){
        	token.clear();
        }
        return "login";
        
        
    }


    @RequestMapping(value="/usersPage")
    public String usersPage(Model model){
        List<CycleBase> bases = cycleBaseService.selectAll();
        List<CycleRoom> rooms = cycleRoomService.selectAll();
        model.addAttribute("bases",bases);
        model.addAttribute("rooms",rooms);

        return "user/users";
    }

    @RequestMapping("/rolesPage")
    public String rolesPage(){
        return "role/roles";
    }

    @RequestMapping("/permissionsPage")
    public String permissionsPage(){
        return "resources/resources";
    }
    
    @RequestMapping("/stationsPage")
    public String stationsPage(){
        return "station/stations";
    }
    @RequestMapping("/groupsPage")
    public String toolGroupPage(){
    	return "group/groups";
    }


    @RequestMapping("/arrturnRulesPage")
    public String applyPage(){
        return "arrturn/rules";
    }

    @RequestMapping(value = "/arrturnRules/detail")
    public String detail(String baseName,Model model){
         model.addAttribute("baseName",baseName);
        List<CycleRoom> cycleRooms = cycleRoomService.selectAll();
        model.addAttribute("rooms",cycleRooms);
        return "arrturn/rulesDetail";
    }

    @RequestMapping("/countPage")
    public String countPage(Model model){
        Integer userId = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
        List<CycleRole> roles = cycleRoleService.queryCycleRoleListByUserId(userId);
        List<CycleBase> bases = cycleBaseService.selectAll();
        List<CycleRoom> rooms = cycleRoomService.selectAll();
        model.addAttribute("roles", roles);
        model.addAttribute("bases", bases);
        model.addAttribute("rooms", rooms);
        return "count/index";
    }

    private Model indexSession(Model model) {
        Integer userId = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
        List<CycleRole> roles = cycleRoleService.queryCycleRoleListByUserId(userId);
        List<CycleBase> bases = cycleBaseService.selectAll();
        List<CycleRoom> rooms = cycleRoomService.selectAll();
        model.addAttribute("roles", roles);
        model.addAttribute("bases", bases);
        model.addAttribute("rooms", rooms);
    return  model;
    }


    @RequestMapping("/arrturnPlan")
    public String arrturnPlan(Model model){
        List<CycleBase> bases = cycleBaseService.selectAll();
        model.addAttribute("bases", bases);
        return "/arrturn/plan";
    }

    @RequestMapping("/arrturnInfoPage")
    public String arrturnInfoPage( Model model){

        List<CycleBase> bases = cycleBaseService.selectAll();
        List<CycleRoom> rooms = cycleRoomService.selectAll();
        model.addAttribute("bases",bases);
        model.addAttribute("rooms",rooms);
        return "/arrturn/info";
    }




    @RequestMapping("/basesPage")
    public String baseStationItemsPage(Model model){
    	List<CycleBase> bases = cycleBaseService.selectAll();
    	model.addAttribute("bases", bases);
        return "/base/bases";
    }

    @RequestMapping("/roomsPage")
    public String roomStationItemsPage(Model model){
        List<CycleRoom> rooms = cycleRoomService.selectAll();
        model.addAttribute("rooms", rooms);
        return "/room/rooms";
    }

    @RequestMapping("/activesPages" )
    public String activesPages(Integer type ,Model model){
        model.addAttribute("status",type);

        List<CycleBase> bases = cycleBaseService.selectAll();
        List<CycleRoom> rooms = cycleRoomService.selectAll();
        model.addAttribute("bases",bases);
        model.addAttribute("rooms",rooms);
        return "/actives/actives_index";
    }


    @RequestMapping("/activesDetailPage" )
    public String activesDetailPage(String activesIdParm ,Model model){
        model.addAttribute("activesIdParm",activesIdParm);
        return "/actives/actives_detail";
    }


    @RequestMapping("/403")
    public String forbidden(){
        return "403";
    }



}
