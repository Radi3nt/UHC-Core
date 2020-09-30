package fr.radi3nt.loupgarouuhc.classes.roles;

import fr.radi3nt.loupgarouuhc.classes.lang.translations.Lang;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.RoleAspect;

public enum RoleSort {


	VILLAGER(WinType.VILLAGE, RoleType.VILLAGER, Lang.VILLAGER.getId()),
	LOUP_GAROU(WinType.LOUP_GAROU, RoleType.LOUP_GAROU, Lang.LOUP_GAROU.getId()),
	PETITE_FILLE(WinType.VILLAGE, RoleType.VILLAGER, Lang.PETITE_FILLE.getId()),
	VP_LOUP(WinType.LOUP_GAROU, RoleType.LOUP_GAROU, Lang.VP_LOUP.getId()),
	VOYANTE(WinType.VILLAGE, RoleType.VILLAGER, Lang.VOYANTE.getId()),
	LGBLANC(WinType.SOLO, RoleType.LOUP_GAROU, Lang.LGBLANC.getId()),
	MONTREUR_OURS(WinType.VILLAGE, RoleType.VILLAGER, Lang.MONTREUR_OURS.getId()),
	ASSASSIN(WinType.SOLO, RoleType.NEUTRAL, Lang.ASSASSIN.getId()),
	LOUP_PERFIDE(WinType.LOUP_GAROU, RoleType.LOUP_GAROU, Lang.LOUP_PERFIDE.getId()),
	SORCIERE(WinType.VILLAGE, RoleType.VILLAGER, Lang.SORCIERE.getId()),
	GUARD(WinType.VILLAGE, RoleType.VILLAGER, "Guard"),
	LG_INFECTE(WinType.VILLAGE, RoleType.LOUP_GAROU, "LoupInfecte"),
	CUPIDON(WinType.VILLAGE, RoleType.NEUTRAL, "Cupidon"),
	LG_FEUTRE(WinType.LOUP_GAROU, RoleType.LOUP_GAROU, Lang.LOUP_FEUTRE.getId()),
	MINEUR(WinType.VILLAGE, RoleType.VILLAGER, Lang.MINEUR.getId()),
	RENARD(WinType.VILLAGE, RoleType.VILLAGER, Lang.RENARD.getId()),
	;


	private final WinType winType;
	private final RoleType roleType;
	private final String id;

	RoleSort(WinType win, RoleType roleType, String id) {
		this.winType = win;
		this.roleType = roleType;
		this.id = id;
	}

	public WinType getWinType() {
		return winType;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public String getId() {
		return id;
	}

	public String getName(Languages language) {
		for (RoleAspect roleAspect : language.getRoleAspects()) {
			if (roleAspect.getRoleSort().id.equals(this.getId())) {
				return roleAspect.getName();
			}
		}
		return language.getDefaultMessage();
	}

	public String getShortDescription(Languages language) {
		for (RoleAspect roleAspect : language.getRoleAspects()) {
			if (roleAspect.getRoleSort().id.equals(this.getId())) {
				return roleAspect.getShort_desc();
			}
		}
		return language.getDefaultMessage();
	}

	public String getRoleDescription(Languages language) {
		for (RoleAspect roleAspect : language.getRoleAspects()) {
			if (roleAspect.getRoleSort().id.equals(this.getId())) {
				return roleAspect.getDesc();
			}
		}
		return language.getDefaultMessage();
	}
}
