package ai.turbochain.ipex.controller;

import ai.turbochain.ipex.entity.FavorSymbol;
import ai.turbochain.ipex.entity.transform.AuthMember;
import ai.turbochain.ipex.service.FavorSymbolService;
import ai.turbochain.ipex.service.LocaleMessageSourceService;
import ai.turbochain.ipex.util.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import static ai.turbochain.ipex.constant.SysConstant.SESSION_MEMBER;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/favor")
public class FavorController {
    @Autowired
    private FavorSymbolService favorSymbolService;

    @Autowired
    private LocaleMessageSourceService msService;

    /**
     * 添加自选
     * @param member
     * @param symbol
     * @return
     */
    @RequestMapping("add")
    public MessageResult addFavor(@SessionAttribute(SESSION_MEMBER) AuthMember member, String symbol){
        if(StringUtils.isEmpty(symbol)){
            return MessageResult.error("symbol cannot be empty");
        }
        FavorSymbol favorSymbol = favorSymbolService.findByMemberIdAndSymbol(member.getId(),symbol);
        if(favorSymbol != null){
            return MessageResult.error("symbol already favored");
        }
        FavorSymbol favor =  favorSymbolService.add(member.getId(),symbol);
        if(favor!= null){
            return MessageResult.success("success");
        }
        return MessageResult.error("error");
    }

    /**
     * 查询当前用户自选
     * @param member
     * @return
     */
    @RequestMapping("find")
    public List<FavorSymbol> findFavor(@SessionAttribute(SESSION_MEMBER) AuthMember member){
        return favorSymbolService.findByMemberId(member.getId());
    }

    /**
     * 删除自选
     * @param member
     * @param symbol
     * @return
     */
    @RequestMapping("delete")
    public MessageResult deleteFavor(@SessionAttribute(SESSION_MEMBER) AuthMember member,String symbol){
        if(StringUtils.isEmpty(symbol)){
            return MessageResult.error("symbol cannot be empty");
        }
        FavorSymbol favorSymbol = favorSymbolService.findByMemberIdAndSymbol(member.getId(),symbol);
        if(favorSymbol == null){
            return MessageResult.error("favor not exists");
        }
        favorSymbolService.delete(member.getId(),symbol);
        return MessageResult.success("success");
    }
}
