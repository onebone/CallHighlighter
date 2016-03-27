package me.onebone.callhighlighter;

/*
 * CallHighlighter: Nukkit plugin which highlight chats which calls player 
 * Copyright (C) 2016  onebone <jyc00410@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class CallHighlighter extends PluginBase implements Listener{
	@Override
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler (ignoreCancelled=true)
	public void onChat(PlayerChatEvent event){
		String message = event.getMessage();
		StringBuilder builder = new StringBuilder();
		
		int length = message.length();
		char currentColor = 'f';
		
		for(int i = 0; i < length; i++){
			char c = message.charAt(i);
			
			if(c == '@'){ // escape
				String username = "";
				for(i++; i < length; i++){
					c = message.charAt(i);
					
					if(check(c)){
						username += c;
					}else break;
				}
				
				if(username.length() > 0){
					Player player = this.getServer().getPlayer(username);
					if(player != null){
						builder.append((event.getPlayer().hasPermission("callhighlighter.emphasize") ? TextFormat.GOLD : TextFormat.AQUA) + 
								"@" + player.getName() + TextFormat.ESCAPE + currentColor);
					}else{
						builder.append("@" + username);
					}
				}else{
					builder.append('@');
				}
				
				if(i < length){
					builder.append(c);
				}
			}else if(c == TextFormat.ESCAPE){
				builder.append(c);
				if(message.length() >= i){
					c = message.charAt(++i);
					builder.append(c);
					
					if('0' <= c && c <= '9' || 'a' <= c && c <= 'f'){
						currentColor = c;
					}
				}
			}else{
				builder.append(c);
			}
		}
		
		event.setMessage(builder.toString());
	}
	
	public static boolean check(char c){
		return ('a' <= c && c <= 'z') ||
				('A' <= c && c <= 'Z') ||
				('0' <= c && c <= '9') || c == '_';
	}
}
