package kr.or.iei.admin.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.iei.admin.model.dto.AdminListData;
import kr.or.iei.admin.model.dao.AdminDao;
import kr.or.iei.member.model.dao.MemberDao;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.notice.model.dto.Notice;
import kr.or.iei.notice.model.dto.NoticeListData;
import kr.or.iei.store.model.dao.StoreDao;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.service.StoreService;

@Service
public class AdminService {
	@Autowired
	private AdminDao adminDao;
	@Autowired
	private StoreDao storeDao;
	
	public AdminListData selectAllMember(int reqPage) {
		//List list = adminDao.selectAllMember();
		//reqPage : 사용자가 요청한 페이지의 번호
		//한 페이지당 보여줄 게시물 수 지정 ->10개
		int numPerPage = 10;
		//reqPage 1페이지 -> rownum 1 ~ 10 

		//reqPage 2페이지 -> rownum 11 ~ 20
		
		//reqPage 3페이지 -> rownum 21 ~ 30
		
		//reqPage 4페이지 -> rownum 31 ~ 40
				
		int end = reqPage*numPerPage;
		int start = end - numPerPage + 1;
		//요청 페이지에 필요한 게시물 목록 조회
		List list = adminDao.selectMemberList(start,end);
				
			
		//페이지 네비게이션 제작
		//전체 몇개 페이지가 있는지 계산 -> 1. 총 게시물 수 조회
				
		//총게시물 수 조회
		int totalCount = adminDao.selectAllMemberCount();
		//전체페이지 계산
		int totalPage = 0;
		if(totalCount%numPerPage==0) {
			totalPage = totalCount/numPerPage;
		}else {
			totalPage = totalCount/numPerPage + 1;
		}
				
		//네비게이션 사이즈
		int pageNaviSize = 5;
		
		//페이지 네비게이션 시작번호
		//reqPage 1 ~ 5 : 1 2 3 4 5			->1
		//regPage 6 ~ 10 : 6 7 8 9 10		->6
		//reqPage 11 ~ 15 : 11 12 13 14 15	->11
		//페이지 네비게이션 시작번호
		int pageNo = ((reqPage - 1)/pageNaviSize)*pageNaviSize +1;
				
		//페이지 네비게이션 제작 시작(html코드작성)
		String pageNavi = "<ul class='pagination circle-style'>";
		//이전버튼(pageNo가 1페이지면 이전페이지가 없으므로 아닐때만 생성)
		if(pageNo!=1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/admin/allMember?reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}
		//페이지 숫자 생성
			for(int i=0;i<pageNaviSize;i++) {
				if(pageNo == reqPage) {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item active-page' href='/admin/allMember?reqPage="+(pageNo)+"'>";
					pageNavi += pageNo;
					pageNavi += "</a></li>";
				}else {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item' href='/admin/allMember?reqPage="+(pageNo)+"'>";
					pageNavi += pageNo;
					pageNavi += "</a></li>";
				}
				pageNo++;
				//페이지를 만들다가 최종페이지를 출력했으면 더이상 반복하지 않고 종료
				if(pageNo > totalPage) {
					break;
				}
			}
				
			//다음버튼(출력한 번호+1 한 숫자가 최종페이지보다 같거나 작으면(최종페이지를 아직 출력하지 않았으면))
			if(pageNo <= totalPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/admin/allMember?reqPage="+(pageNo)+"'>";
				pageNavi += "<span class='material-icons'>chevron_right</span>";
				pageNavi += "</a></li>";
			}
			pageNavi += "</ul>";
				
			//list, pageNavi 두개를 모두 리턴 -> java의 메소드는 리턴타입이 1개 -> 바로리턴불가능
			//-> 두개를 저장하는 객체를 만들어서 리턴
			AdminListData ald = new AdminListData(list, pageNavi);
			return ald;
	}
	

	
	public int changeLevel(Member m) {
		int result = adminDao.changeLevel(m);
		return result;
	}

	public int changeStoreLevel(Store store) {
		int result = adminDao.changeStoreLevel(store);
		return result;
	}
	
	
	public AdminListData searchMember(int reqPage, String type, String keyword) {
		// reqPage : 사용자가 요청한 페이지의 번호
		// 한 페이지당 보여줄 게시물 수 지정 ->10개
		int numPerPage = 10;
		// reqPage 1페이지 -> rownum 1 ~ 10
		// reqPage 2페이지 -> rownum 11 ~ 20
		// reqPage 3페이지 -> rownum 21 ~ 30
		// reqPage 4페이지 -> rownum 31 ~ 40

		int end = reqPage * numPerPage;
		int start = end - numPerPage + 1;
				
		// 요청 페이지에 필요한 게시물 목록 조회
		List list = null;
		if(type.equals("all")) {
			list = adminDao.selectSearchAll(start,end,keyword);
		}else if(type.equals("id")) {
			list = adminDao.selectSearchId(start,end,keyword);
		}else if(type.equals("email")) {
			list = adminDao.selectSearchEmail(start,end,keyword);
		}else if(type.equals("name")) {
			list = adminDao.selectSearchName(start,end,keyword);
		}

		// 페이지 네비게이션 제작
		// 전체 몇개 페이지가 있는지 계산 -> 1. 총 게시물 수 조회
		// 총게시물 수 조회
		int totalCount =0;
		if(type.equals("all")) {
			totalCount = adminDao.allTotalCount(keyword);
		}else if(type.equals("id")) {
			totalCount = adminDao.idTotalCount(keyword);
		}else if(type.equals("email")) {
			totalCount = adminDao.emailTotalCount(keyword);
		}else if(type.equals("name")) {
			totalCount = adminDao.nameTotalCount(keyword);
		}
		
				
		// 전체페이지 계산
		int totalPage = 0;
		if (totalCount % numPerPage == 0) {
				totalPage = totalCount / numPerPage;
		} else {
				totalPage = totalCount / numPerPage + 1;
		}

		// 네비게이션 사이즈
		int pageNaviSize = 5;

		// 페이지 네비게이션 시작번호
		// reqPage 1 ~ 5 : 1 2 3 4 5 ->1
		// regPage 6 ~ 10 : 6 7 8 9 10 ->6
		// reqPage 11 ~ 15 : 11 12 13 14 15 ->11
		// 페이지 네비게이션 시작번호
		int pageNo = ((reqPage - 1) / pageNaviSize) * pageNaviSize + 1;

		// 페이지 네비게이션 제작 시작(html코드작성)
		String pageNavi = "<ul class='pagination circle-style'>";
		// 이전버튼(pageNo가 1페이지면 이전페이지가 없으므로 아닐때만 생성)
		if (pageNo != 1) {
			pageNavi += "<li>";
								//주소 상황에 맞게 적어주기
			pageNavi += "<a class='page-item' href='/admin/search?reqPage=" + (pageNo - 1) + "&type="+type+"&keyword="+keyword+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}
				// 페이지 숫자 생성
		for (int i = 0; i < pageNaviSize; i++) {
			if (pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/admin/search?reqPage=" + (pageNo) + "&type="+type+"&keyword="+keyword+"'>";
				pageNavi += pageNo;
						pageNavi += "</a></li>";
					} else {
						pageNavi += "<li>";
						pageNavi += "<a class='page-item' href='/admin/search?reqPage=" + (pageNo) + "&type="+type+"&keyword="+keyword+"'>";
						pageNavi += pageNo;
						pageNavi += "</a></li>";
					}
					pageNo++;
					// 페이지를 만들다가 최종페이지를 출력했으면 더이상 반복하지 않고 종료
					if (pageNo > totalPage) {
						break;
					}
				}

				// 다음버튼(출력한 번호+1 한 숫자가 최종페이지보다 같거나 작으면(최종페이지를 아직 출력하지 않았으면))
				if (pageNo <= totalPage) {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item' href='/admin/search?reqPage=" + (pageNo - 1) + "&type="+type+"&keyword="+keyword+"'>";
					pageNavi += "<span class='material-icons'>chevron_right</span>";
					pageNavi += "</a></li>";
				}
				pageNavi += "</ul>";

				// list, pageNavi 두개를 모두 리턴 -> java의 메소드는 리턴타입이 1개 -> 바로리턴불가능
				// -> 두개를 저장하는 객체를 만들어서 리턴
				AdminListData ald = new AdminListData(list, pageNavi);
				return ald;

	}



	public Member selectOneMember(int memberNo) {
		Member member = adminDao.selectOneMember(memberNo); 
		return member;
	}



	public int memberUpdate(Member member) {
		int result = adminDao.memberUpdate(member);
		return result;
	}



	public AdminListData selectAllStore(int reqPage) {
		int numPerPage = 10;
		int end = reqPage*numPerPage;
		int start = end - numPerPage + 1;
		List list = adminDao.selectStoreList(start,end);
						
		int totalCount = adminDao.selectAllStoreCount();
		int totalPage = 0;
		if(totalCount%numPerPage==0) {
			totalPage = totalCount/numPerPage;
		}else {
			totalPage = totalCount/numPerPage + 1;
		}

		int pageNaviSize = 5;
				
		int pageNo = ((reqPage - 1)/pageNaviSize)*pageNaviSize +1;
				
		String pageNavi = "<ul class='pagination circle-style'>";
		if(pageNo!=1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/admin/allStore?reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/admin/allStore?reqPage="+(pageNo)+"'>";
					pageNavi += pageNo;
					pageNavi += "</a></li>";
			}else {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item' href='/admin/allStore?reqPage="+(pageNo)+"'>";
					pageNavi += pageNo;
					pageNavi += "</a></li>";
			}
			pageNo++;
			if(pageNo > totalPage) {
				break;
			}
		}
						
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/admin/allStore?reqPage="+(pageNo)+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a></li>";
		}
		pageNavi += "</ul>";
						
		AdminListData ald = new AdminListData(list, pageNavi);
		return ald;
	}
	
	public AdminListData searchStore(int reqPage, String type, String keyword) {
		int numPerPage = 10;

		int end = reqPage * numPerPage;
		int start = end - numPerPage + 1;
						
		List list = null;
		if(type.equals("all")) {
			list = adminDao.selectSearchStoreAll(start,end,keyword);
		}else if(type.equals("subway")) {
			list = adminDao.selectSearchStoreSubway(start,end,keyword);
		}else if(type.equals("name")) {
			list = adminDao.selectSearchStoreName(start,end,keyword);
		}
		int totalCount =0;
		if(type.equals("all")) {
			totalCount = adminDao.allStoreTotalCount(keyword);
		}else if(type.equals("subway")) {
			totalCount = adminDao.subwayTotalCount(keyword);
		}else if(type.equals("name")) {
			totalCount = adminDao.storeNameTotalCount(keyword);
		}
				

		int totalPage = 0;
		if (totalCount % numPerPage == 0) {
				totalPage = totalCount / numPerPage;
		} else {
				totalPage = totalCount / numPerPage + 1;
		}

		int pageNaviSize = 5;


		int pageNo = ((reqPage - 1) / pageNaviSize) * pageNaviSize + 1;

		String pageNavi = "<ul class='pagination circle-style'>";
		if (pageNo != 1) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/admin/searchStore?reqPage=" + (pageNo - 1) + "&type="+type+"&keyword="+keyword+"'>";
				pageNavi += "<span class='material-icons'>chevron_left</span>";
				pageNavi += "</a></li>";
		}
		for (int i = 0; i < pageNaviSize; i++) {
			if (pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/admin/searchStore?reqPage=" + (pageNo) + "&type="+type+"&keyword="+keyword+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			} else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/admin/searchStore?reqPage=" + (pageNo) + "&type="+type+"&keyword="+keyword+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			}
			pageNo++;
							
			if (pageNo > totalPage) {
					break;
				}
		}

		if (pageNo <= totalPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/admin/searchStore?reqPage=" + (pageNo - 1) + "&type="+type+"&keyword="+keyword+"'>";
				pageNavi += "<span class='material-icons'>chevron_right</span>";
				pageNavi += "</a></li>";
		}
		pageNavi += "</ul>";

		AdminListData ald = new AdminListData(list, pageNavi);
		return ald;
	}



	public Store adminSelectOneStore(int storeNo) {
		Store store = storeDao.selectGetStore(storeNo);
		List fileList = storeDao.selectEvidenceFile(storeNo);
		store.setFileList(fileList);
		return store;
	}



	public Member selectMemberId(int memberNo) {
		Member member= adminDao.selectOneMember(memberNo);
		if(member != null) {		
			return member;
		}else {
			return null;
		}
	}



	public int updateStoreStatus(int storeNo) {
		int result = adminDao.updateStoreStatus(storeNo);
		return result;
	}

	public Store selectStoreNo(int memberNo) {
		Store store= adminDao.selectStoreNo(memberNo);
		if(store != null) {		
			return store;
		}else {
			return null;
		}
	}
}
