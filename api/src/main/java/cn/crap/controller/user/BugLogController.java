package cn.crap.controller.user;

import cn.crap.adapter.BugLogAdapter;
import cn.crap.dto.BugLogDTO;
import cn.crap.enu.PremissionEnum;
import cn.crap.framework.JsonResult;
import cn.crap.framework.MyException;
import cn.crap.framework.base.BaseController;
import cn.crap.framework.interceptor.AuthPassport;
import cn.crap.model.BugLogPO;
import cn.crap.model.Project;
import cn.crap.query.BugLogQuery;
import cn.crap.service.BugLogService;
import cn.crap.utils.Page;
import cn.crap.utils.TableField;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/bugLog")
public class BugLogController extends BaseController{
    // TODO 权限目前只要项目成员即可操作
    @Autowired
    private BugLogService bugLogService;


    /**
     * bug列表
     * @return
     * @throws MyException
     */
    @RequestMapping("/list.do")
    @ResponseBody
    @AuthPassport
    public JsonResult list(@ModelAttribute BugLogQuery query) throws Exception {
        if (query.getBugId() == null){
            return new JsonResult().data(Lists.newArrayList()).page(new Page(query));
        }

        Project project = getProject(query);
        checkPermission(project, PremissionEnum.READ);
        query.setPageSize(100);
        query.setSort(TableField.SORT.CREATE_TIME_DES);

        Page page = new Page(query);
        List<BugLogPO> bugLogPOList = bugLogService.select(query, page);
        page.setAllRow(bugLogService.count(query));

        List<BugLogDTO> dtoList = BugLogAdapter.getDto(bugLogPOList);
        return new JsonResult().data(dtoList).page(page);
    }

}