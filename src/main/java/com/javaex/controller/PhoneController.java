package com.javaex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaex.dao.PhoneDao;
import com.javaex.vo.PersonVo;

@Controller
@RequestMapping(value="/phone")
public class PhoneController {

	//필드
	@Autowired 
	private PhoneDao phoneDao;
	//생성자
	//메소드g/s
	
	/**메소드 일반** 메소드 일반(메소드마다 기능 1개씩) --> 기능마다 url 부여**/
	//리스트
	
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String list(Model model) {
		System.out.println("list");
		
		//dao를 통해 리스트 가져오기
		
		List<PersonVo> personList = phoneDao.getPersonList();
		System.out.println(personList.toString());
		
		//model --> data 를 보내는 방법 --> 담아 놓으면 된다
		model.addAttribute("pList", personList);
		
		
		return "list";
	}
	
	//등록폼
	@RequestMapping(value = "/writeForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String writeForm() {
		System.out.println("writeForm");
		
		return "writeForm";
	}	
	 //http://localhost:8088/phonebook3/phone/write?name=최태현&hp=010-1111-1111&company=02-2222-2222
	//등록
	@RequestMapping(value = "/write", method = {RequestMethod.GET, RequestMethod.POST})
	public String write(@RequestParam("name") String name, 
						@RequestParam("hp") String hp, 
						@RequestParam("company") String company) {
		
		System.out.println("write");
		
		//vo 묶고 확인
		PersonVo personVo = new PersonVo(name, hp, company);
		System.out.println(personVo.toString());
		
		//Dao에 묶은 vo
		//PhoneDao phoneDao = new PhoneDao(); AutoWired
		phoneDao.personInsert(personVo);
		
		return "redirect:/phone/list";
	}
	
	//수정폼 --> modifyForm
	@RequestMapping(value = "/modifyForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String modifyForm(Model model, @RequestParam("personId") int personId) { //일단은 되는데 저렇게 쓰는게 맞나 확인
		System.out.println("modifyForm");
		System.out.println(personId);
		
		//id로 정보 가져오기
		//PhoneDao phoneDao = new PhoneDao();  AutoWired
		PersonVo personVo = phoneDao.getPerson(personId);
		
		System.out.println(personVo.toString());
		
		//model로 보내줌 personVo를 -->model--> dispatcherServlet --> view(.jsp)
		model.addAttribute("personVo", personVo);
		
		//포워드
		return "modifyForm";
	}

	//수정 --> modify2 이전버전 --> @RequestParam
	@RequestMapping(value = "/modify2", method = {RequestMethod.GET, RequestMethod.POST})
	public String modify(@RequestParam("name") String name,
						 @RequestParam("hp") String hp,
						 @RequestParam("company") String company,
						 @RequestParam("id") int personId) {
							
		System.out.println("modify2");
		
		//Vo 묶고 
		PersonVo personVo = new PersonVo(personId, name, hp, company);
		System.out.println(personVo.toString());
		
		//Dao
		//PhoneDao phoneDao = new PhoneDao();  AutoWired
		phoneDao.personUpdate(personVo);
		
		return "redirect:/phone/list";
	}
	
	
	//수정 --> modify --> @ModelAttribute --> 파라미터를 넣는 작업까지 해줌
		@RequestMapping(value = "/modify", method = {RequestMethod.GET, RequestMethod.POST})
		public String modify(@ModelAttribute PersonVo personVo) {
								
			System.out.println("modify");
			
			//Vo 묶고 --파라미터 값을 vo로 묶는게 관행임 --> 이것을 스프링에서 자동으로 해줌
			System.out.println(personVo.toString());
			
			//Dao
			//PhoneDao phoneDao = new PhoneDao();  AutoWired
			phoneDao.personUpdate(personVo);
			
			return "redirect:/phone/list";
		}
	
	
	//삭제 --> delete -->약식
	@RequestMapping("/delete2")
	public String delete2(@RequestParam("personId") int personId) {
		System.out.println("delete");
		
		
		//dao
		//PhoneDao phoneDao = new PhoneDao();  AutoWired
		phoneDao.personDelete(personId);
		
		return "redirect:/phone/list";
		
	}

	//삭제 --> delete -->약식
		@RequestMapping(value = "/delete/{personId}", method = {RequestMethod.GET, RequestMethod.POST})
		public String delete(@PathVariable("personId") int personId) {
			System.out.println("delete");
			System.out.println(personId);
			
			//dao
			//PhoneDao phoneDao = new PhoneDao();  AutoWired
			phoneDao.personDelete(personId);
			
			return "redirect:/phone/list";
			
		}

	
	
}
